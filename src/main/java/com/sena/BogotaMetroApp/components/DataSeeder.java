package com.sena.BogotaMetroApp.components;

import com.sena.BogotaMetroApp.persistence.models.Linea;
import com.sena.BogotaMetroApp.persistence.models.estacion.Estacion;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLinea;
import com.sena.BogotaMetroApp.persistence.models.estacion.EstacionLineaId;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionLineaRepository;
import com.sena.BogotaMetroApp.persistence.repository.estacion.EstacionRepository;
import com.sena.BogotaMetroApp.persistence.repository.linea.LineaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final LineaRepository lineaRepo;
    private final EstacionRepository estacionRepo;
    private final EstacionLineaRepository estacionLineaRepo;

    @Override
    public void run(String... args) throws Exception {
        if (lineaRepo.count() == 0) {
            cargarLinea1();
            cargarLinea2();
        }
    }

    private void cargarLinea1() {
        // 1. Crear Línea
        Linea l1 = new Linea();
        l1.setNombre("Línea 1");
        l1.setColor("#FF0000"); // Rojo
        l1.setFrecuenciaMinutos(3);
        l1 = lineaRepo.save(l1);

        // 2. Crear Estaciones (Ejemplo con las primeras 3)
        crearYAsociarEstacion("Av. Villavicencio / Cra 94 - Cra 93", 4.630, -74.180, l1, 1);
        crearYAsociarEstacion("Av. Villavicencio / Cra 86b - Cra 86g", 4.635, -74.175, l1, 2);
        crearYAsociarEstacion("Av. Villavicencio / Cra 80d - Cra 80g", 4.640, -74.170, l1, 3);
        crearYAsociarEstacion("Av. Primero de Mayo / Calle 42 sur - Calle 42c sur",4.588,-74.108,l1,4);
        crearYAsociarEstacion("Av. Primero de Mayo / Calle 40 sur - Calle 39 sur",4.592,-74.108,l1,5);
        crearYAsociarEstacion("Av. Primero de Mayo / Av. Boyacá - Carrera 72c",4.600,-74.118,l1,6);
        crearYAsociarEstacion("Av. Primero de Mayo / Av. 68 - Cra 52c",4.600,-74.088,l1,7);
        crearYAsociarEstacion("Av. Primero de Mayo / Glorieta Cra 50",4.600,-74.078,l1,8);
        crearYAsociarEstacion("NQS / Dg. 16 sur - Calle 17A bis sur",4.620,-74.078,l1,9);
        crearYAsociarEstacion("Calle 1 / Cra 24 - Cra 24c",4.603,-74.063,l1,10);
        crearYAsociarEstacion("Av. Caracas / Calle 2 - Calle 3",4.607,-74.074,l1,11);
        crearYAsociarEstacion("Av. Caracas / Calle 11 - Calle 13",4.621,-74.074,l1,12);
        crearYAsociarEstacion("Av.Caracas / Calle 24a - Calle 26",4.640,-74.074,l1,13);
        crearYAsociarEstacion("Av.Caracas / Calle 42 - Calle 44",4.673,-74.074,l1,14);
        crearYAsociarEstacion("Av. Caracas / Calle 61 - Calle 63",4.697,-74.074,l1,15);
        crearYAsociarEstacion("Av. Caracas / Calle 72 - Calle 74",4.712,-74.074,l1,16);

        // ... Agrega las otras 13 estaciones aquí ...
    }

    private void cargarLinea2() {
        Linea l2 = new Linea();
        l2.setNombre("Línea 2");
        l2.setColor("#FFFF00"); // Amarillo
        l2.setFrecuenciaMinutos(4);
        l2 = lineaRepo.save(l2);

        crearYAsociarEstacion("Calle 72 con Av. Caracas", 4.656, -74.060, l2, 1);
        crearYAsociarEstacion("Calle 72 con Av. NQS", 4.660, -74.070, l2, 2);
        crearYAsociarEstacion("Calle 72 con Av. Carrera 68",4.657,-74.092,l2,3 );
        crearYAsociarEstacion("Calle 72 con Av. Boyacá",4.657,-74.118,l2,4);
        crearYAsociarEstacion("Calle 72 con carrera 80",4.657,-74.125,l2,5);
        crearYAsociarEstacion("Av. Ciudad de Cali con calle 80",4.700,-74.110,l2,6);
        crearYAsociarEstacion("Av. Ciudad de Cali con calle 90",4.710,-74.110,l2,7);
        crearYAsociarEstacion("Av. Ciudad de Cali con carrera 93",4.715,-74.102,l2,8);
        crearYAsociarEstacion("ALO con calle 129 D",4.745,-74.090,l2,9);
        crearYAsociarEstacion("ALO con calle 139",4.760,-74.085,l2,10);
        crearYAsociarEstacion("Av. Calle 145 con carrera 141 B",4.775,-74.075,l2,11);

    }

    private void crearYAsociarEstacion(String nombre, double lat, double lon, Linea linea, int orden) {
        Estacion e = new Estacion();
        e.setNombre(nombre);
        e.setLatitud(BigDecimal.valueOf(lat));
        e.setLongitud(BigDecimal.valueOf(lon));
        e.setEsAccesible(true);
        e.setTipo("ELEVADA"); // O SUBTERRANEA para linea 2
        e = estacionRepo.save(e);

        EstacionLinea relacion = new EstacionLinea();
        relacion.setId(new EstacionLineaId(linea.getId(), e.getId()));
        relacion.setLinea(linea);
        relacion.setEstacion(e);
        relacion.setOrden(orden); // IMPORTANTE
        estacionLineaRepo.save(relacion);
    }
}
