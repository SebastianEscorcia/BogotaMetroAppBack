package com.sena.BogotaMetroApp.persistence.models.transaccion;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;


import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import com.sena.BogotaMetroApp.utils.enums.MonedaEnum;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long id;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", length = 3, nullable = false)
    private MonedaEnum moneda = MonedaEnum.COP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_fk", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarjeta_fk", nullable = false)
    private TarjetaVirtual tarjetaVirtual;

    public abstract MedioPagoEnum obtenerMedioDePago();
}
