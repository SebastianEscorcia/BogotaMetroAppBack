package com.sena.BogotaMetroApp.persistence.repository.horariosistema;

import com.sena.BogotaMetroApp.persistence.models.horariosistema.HorarioSistema;
import com.sena.BogotaMetroApp.utils.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HorarioSistemaRepository extends JpaRepository<HorarioSistema, Long> {
    Optional<HorarioSistema> findByDiaAndActivoTrue(DiaSemana dia);

    // Busca si existe otro horario activo con el mismo día, excluyendo el horario actual (por ID)
    Optional<HorarioSistema> findByDiaAndActivoTrueAndIdNot(DiaSemana dia, Long id);
}
