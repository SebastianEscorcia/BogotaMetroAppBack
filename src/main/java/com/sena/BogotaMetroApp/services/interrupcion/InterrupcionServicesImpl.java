package com.sena.BogotaMetroApp.services.interrupcion;

import com.sena.BogotaMetroApp.errors.ErrorCodeEnum;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionRequestDTO;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionResponseDTO;
import com.sena.BogotaMetroApp.mapper.InterrupcionMapper;
import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.interrupcion.Interrupcion;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.interrupcion.InterrupcionRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import com.sena.BogotaMetroApp.presentation.dto.interrupcion.InterrupcionUpdateDTO;
import com.sena.BogotaMetroApp.services.exception.interrupcion.InterrupcionException;
import com.sena.BogotaMetroApp.utils.enums.EstadoInterrupcionEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterrupcionServicesImpl implements IInterrupcionServices {

    private final InterrupcionRepository repository;
    private final EstacionRepository estacionRepository;
    private final LineaRepository lineaRepository;
    private final InterrupcionMapper mapper;

    private final InterrupcionNotificationService notificationService;

    @Override
    @Transactional
    public InterrupcionResponseDTO crear(InterrupcionRequestDTO dto) {


        if (dto.getIdEstacion() == null && dto.getIdLinea() == null) {
            throw new InterrupcionException(ErrorCodeEnum.INTERRUPCION_ESTACION_LINEA_REQ);
        }

        Interrupcion inter = new Interrupcion();
        if (dto.getIdEstacion() != null) {
            Estacion estacion = estacionRepository.findById(dto.getIdEstacion())
                    .orElseThrow(() -> new InterrupcionException(ErrorCodeEnum.RUTA_ESTACION_NOT_FOUND));
            inter.setEstacion(estacion);
        }

        if (dto.getIdLinea() != null) {
            Linea linea = lineaRepository.findById(dto.getIdLinea())
                    .orElseThrow(() -> new InterrupcionException(ErrorCodeEnum.LINEA_NOT_FOUND));
            inter.setLinea(linea);
        }

        inter.setTipo(dto.getTipo());
        inter.setDescripcion(dto.getDescripcion());
        inter.setInicio(dto.getInicio());
        inter.setFin(dto.getFin());
        inter.setEstado(EstadoInterrupcionEnum.ACTIVA);
        inter.setActivo(true);

        Interrupcion guardada = repository.save(inter);
        InterrupcionResponseDTO responseDTO = mapper.toDTO(guardada);
        notificationService.notificarNuevaInterrupcion(responseDTO);
        return responseDTO;
    }

    @Override
    public List<InterrupcionResponseDTO> listar() {
        return repository.findByActivoTrue().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Interrupcion inter = repository.findById(id)
                .orElseThrow(() -> new InterrupcionException(ErrorCodeEnum.INTERRUPCION_NOT_FOUND));

        inter.setActivo(false);
        repository.save(inter);

        notificationService.notificarEliminacion(id);
    }


    @Transactional
    @Override
    public void marcarComoSolucionada(Long id) {

        Interrupcion inter = repository.findById(id)
                .orElseThrow(() -> new InterrupcionException(ErrorCodeEnum.INTERRUPCION_NOT_FOUND));

        if (!inter.getActivo()) {
            throw new InterrupcionException(ErrorCodeEnum.INTERRUPCION_YA_ELIMINADA);
        }

        inter.setEstado(EstadoInterrupcionEnum.SOLUCIONADA);
        inter.setFin(LocalDateTime.now());

        Interrupcion actualizada = repository.save(inter);


        notificationService.notificarActualizacion(mapper.toDTO(actualizada));
    }

    @Override
    public InterrupcionResponseDTO actualizar(Long id, InterrupcionUpdateDTO dto) {
        Interrupcion inter = repository.findById(id)
                .orElseThrow(() -> new InterrupcionException(ErrorCodeEnum.INTERRUPCION_NOT_FOUND));
        if (!inter.getActivo()) {
            throw new InterrupcionException(ErrorCodeEnum.INTERRUPCION_YA_ELIMINADA);
        }
        if (dto.getTipo() != null) inter.setTipo(dto.getTipo());
        if (dto.getDescripcion() != null) inter.setDescripcion(dto.getDescripcion());
        if (dto.getInicio() != null) inter.setInicio(dto.getInicio());
        if (dto.getFin() != null) inter.setFin(dto.getFin());
        if (dto.getEstado() != null) inter.setEstado(dto.getEstado());

        Interrupcion actualizada = repository.save(inter);
        InterrupcionResponseDTO responseDTO = mapper.toDTO(actualizada);
        notificationService.notificarActualizacion(responseDTO);

        return responseDTO;
    }
}
