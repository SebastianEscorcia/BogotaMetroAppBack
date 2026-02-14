package com.sena.BogotaMetroApp.persistence.models.tarifasistema;

import com.sena.BogotaMetroApp.persistence.models.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarifasistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarifaSistema extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long id;
    @Column(name = "valor_tarifa", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTarifa;

    @Column(name = "descripcion", length = 255)
    private String descripcion = "Tarifa estándar Metro";

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name= "updated_at", nullable = false)
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;


}
