package com.sena.BogotaMetroApp.services.qr;

import com.sena.BogotaMetroApp.externalservices.cache.IQrCacheService;
import com.sena.BogotaMetroApp.externalservices.cache.QrRedisQrCacheServiceImpl;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.presentation.dto.QrCacheDTO;
import com.sena.BogotaMetroApp.presentation.dto.qr.QrResponseDTO;
import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.mapper.qr.QrMapper;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import com.sena.BogotaMetroApp.persistence.repository.qr.QrRepository;

import com.sena.BogotaMetroApp.services.exception.qr.QrException;
import com.sena.BogotaMetroApp.services.exception.usuario.UsuarioException;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import com.sena.BogotaMetroApp.utils.validators.QrValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QrServiceImpl implements IQrService {

    private final QrRepository qrRepository;
    private final UsuarioRepository usuarioRepository;
    private final QrMapper qrMapper;
    private final QrValidator qrValidator;
    private final IQrNoUsadoService qrNoUsadoService;
    private final IQrCacheService<QrCacheDTO, Long> qrCacheService;
    private static final int MINUTOS_EXPIRACION = 15;


    /**
     *  Genera un código QR de acceso para el usuario con el correo electrónico proporcionado.
     *  Si el usuario ya tiene un código QR activo y válido, se devuelve ese código,
     *  Si el usuario tiene un código QR no consumido pero expirado, se mueve a la tabla de códigos QR no usados y se crea uno nuevo,
     *  y si ya  tiene un condigo qr activo, se devuelve ese.
     * @param email Correo electrónico del usuario para el cual se generará el código QR.
     * @return QrResponseDTO
     */
    @Override
    @Transactional
    public QrResponseDTO generarQrAcceso(String email) {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new UsuarioException(ErrorCodeEnum.USUARIO_NOT_FOUND));

        Long usuarioId = usuario.getId();
        LocalDateTime ahora = LocalDateTime.now();

        // 1. Intentar obtener desde caché
        QrResponseDTO cacheado = obtenerDesdeCache(usuarioId, ahora);
        if (cacheado != null) {
            return cacheado;
        }

        // 2. Buscar QR activo en BD
        Optional<Qr> activoOpt = qrRepository.findQrActivoByUsuario(usuarioId, ahora);
        if (activoOpt.isPresent()) {
            return cachearYRetornar(activoOpt.get());
        }

        // 3. Procesar QR no consumido (puede estar expirado)
        Optional<Qr> ultimoOpt = qrRepository.findLatestNotConsumedQrForUserForUpdate(usuarioId);
        if (ultimoOpt.isPresent()) {
            return procesarQrExistente(ultimoOpt.get(), usuario, ahora);
        }

        // 4. No existe ningún QR → crear nuevo
        return crearYCachearNuevoQr(usuario);
    }


    @Override
    public Qr validarYObtenerPorContenido(String contenidoQr) {
        Qr qr = qrRepository.findByContenidoQr(contenidoQr)
                .orElseThrow(() -> new QrException(ErrorCodeEnum.QR_NOT_FOUND));

        qrValidator.validarQrParaTorniquete(qr);

        return qr;
    }

    @Override
    @Transactional
    public Qr consumirQr(Qr qrB) {

        Qr qr = qrRepository.findById(qrB.getId())
                .orElseThrow(() -> new QrException(ErrorCodeEnum.QR_NOT_FOUND));

        //  Idempotencia: ya consumido -> igual invalidamos cache
        if (qr.getConsumido()) {

            QrCacheDTO dto = new QrCacheDTO();
            dto.setUsuarioId(qr.getUsuario().getId());
            dto.setContenido(qr.getContenidoQr());

            qrCacheService.invalidate(dto.getUsuarioId());
            if(qrCacheService instanceof QrRedisQrCacheServiceImpl impl){
                impl.invalidateByCodigo(dto.getContenido());
            }
            return qr;
        }


        qr.setConsumido(true);

        // JPA incrementa @Version
        Qr saved = qrRepository.save(qr);

        // Invalidar cache correctamente
        QrCacheDTO dto = new QrCacheDTO();
        dto.setUsuarioId(saved.getUsuario().getId());
        dto.setContenido(saved.getContenidoQr());

        qrCacheService.invalidate(dto.getUsuarioId());

        return saved;
    }


    private QrResponseDTO obtenerDesdeCache(Long usuarioId, LocalDateTime ahora) {
        Optional<QrCacheDTO> cacheOpt = qrCacheService.get(usuarioId);

        if (cacheOpt.isEmpty()) {
            return null;
        }

        QrCacheDTO cache = cacheOpt.get();
        boolean esValido = cache.getFechaExpiracion().isAfter(ahora) && !cache.isConsumido();

        if (esValido) {
            return qrMapper.toDTOFromCache(cache);
        }

        // Cache inválido, invalidar
        qrCacheService.invalidate(usuarioId);
        if (cache.getContenido() != null) {
            qrCacheService.invalidateFull(cache);
        }

        return null;
    }

    private QrResponseDTO procesarQrExistente(Qr qr, Usuario usuario, LocalDateTime ahora) {
        boolean estaExpirado = qr.getFechaExpiracion().isBefore(ahora);

        if (estaExpirado) {
            moverAHistoricoYEliminar(qr);
            return crearYCachearNuevoQr(usuario);
        }

        return cachearYRetornar(qr);
    }

    private void moverAHistoricoYEliminar(Qr qr) {
        qrNoUsadoService.moverQrNoUsadoAndExpirado(qr);
        qrRepository.delete(qr);
        qrRepository.flush();
    }

    private QrResponseDTO crearYCachearNuevoQr(Usuario usuario) {
        Qr nuevo = crearNuevoQr(usuario);
        return cachearYRetornar(nuevo);
    }

    private QrResponseDTO cachearYRetornar(Qr qr) {
        qrCacheService.cache(qrMapper.toCacheDTO(qr));
        return qrMapper.toDTO(qr);
    }

    private Qr crearNuevoQr(Usuario usuario) {
        Qr qr = new Qr();
        qr.setUsuario(usuario);
        qr.setTipo(TipoQr.ACCESO);
        qr.setFechaGeneracion(LocalDateTime.now());
        qr.setFechaExpiracion(LocalDateTime.now().plusMinutes(MINUTOS_EXPIRACION));
        qr.setConsumido(false);
        qr.setContenidoQr(qrMapper.generarContenidoQr(TipoQr.ACCESO, usuario.getId(), usuario.getId()));

        return qrRepository.save(qr);
    }
   
}
