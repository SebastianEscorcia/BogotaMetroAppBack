package com.sena.BogotaMetroApp.persistence.repository.operador;

import com.sena.BogotaMetroApp.persistence.models.operador.Operador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {

}
