package com.sena.BogotaMetroApp.services.recarga;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.transaccion.Recarga;
import com.sena.BogotaMetroApp.persistence.repository.transaccion.RecargaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import static org.mockito.Mockito.*;

public class RecargaServiceImplTest {

    @Mock
    private RecargaRepository recargaRepository;

    @InjectMocks
    private RecargaServiceImpl recargaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarRecarga_existe() {

        Long id = 1L;

        Recarga recarga = new Recarga();
        TarjetaVirtual tarjeta = new TarjetaVirtual();

        when(recargaRepository.findById(id))
                .thenReturn(Optional.of(recarga));

        recargaService.guardarRecarga(id, tarjeta);

        verify(recargaRepository).save(recarga);
    }

    @Test
    void testGuardarRecarga_noExiste() {

        Long id = 1L;
        TarjetaVirtual tarjeta = new TarjetaVirtual();

        when(recargaRepository.findById(id))
                .thenReturn(Optional.empty());

        recargaService.guardarRecarga(id, tarjeta);

        verify(recargaRepository, never()).save(any());
    }
}