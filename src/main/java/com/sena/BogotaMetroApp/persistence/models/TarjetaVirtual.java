package com.sena.BogotaMetroApp.persistence.models;

import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.utils.enums.EstadoTarjetaEnum;
import com.sena.BogotaMetroApp.utils.logic.TarjetaUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tarjetasvirtuales")
@Getter
@Setter
public class TarjetaVirtual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarjeta")
    private Long idTarjeta;
    // Usamos UUID para generar un número único de tarjeta interno
    @Column(name = "numero_tarjeta", unique = true, nullable = false, length = 36)
    private String numeroTarjeta;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false) // ACTIVA, BLOQUEADA, INACTIVA
    private EstadoTarjetaEnum estado;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    // RELACIÓN: Una tarjeta le pertenece a un pasajero.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajeroFK", nullable = false, unique = true)
    private Pasajero pasajero;

    // Método utilitario para inicializar datos antes de persistir
    @PrePersist
    public void prePersist() {
        if (this.numeroTarjeta == null) {
            //this.numeroTarjeta = UUID.randomUUID().toString();
            this.numeroTarjeta = TarjetaUtil.generarNumeroTarjeta();
        }
        if (this.saldo == null) {
            this.saldo = BigDecimal.ZERO;
        }
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDate.now();
        }
        if (this.estado == null) {
            this.estado = EstadoTarjetaEnum.ACTIVA;
        }
    }
}
