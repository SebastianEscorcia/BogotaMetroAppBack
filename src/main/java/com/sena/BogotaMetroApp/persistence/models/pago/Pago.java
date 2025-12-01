package com.sena.BogotaMetroApp.persistence.models.pago;
import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.persistence.models.Usuario;

import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detallePagos")
@Getter @Setter
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "valor_pagado", nullable = false)
    private BigDecimal  valorPagado;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(length = 100)
    private String descripcion;

    @Column(name = "referencia_pasarela", nullable = false)
    private String referenciaPasarela;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @ManyToOne
    @JoinColumn(name = "id_pasarelaFK" , nullable = false)
    private Pasarela pasarela;

    @ManyToOne
    @JoinColumn(name = "id_usuarioFK", nullable = false)
    private Usuario usuario;

    @Column(name = "medio_de_pago")
    private String medioDePago;

    @ManyToOne
    @JoinColumn(name = "id_tarjetaFK", nullable = false)
    private TarjetaVirtual tarjetaVirtual;
}
