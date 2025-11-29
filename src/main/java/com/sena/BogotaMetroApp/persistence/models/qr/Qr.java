package com.sena.BogotaMetroApp.persistence.models.qr;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pago.Pago;
import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "qrs")
@Getter @Setter
public class Qr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_qr")
    private Long id;

    @Column(name = "contenido_qr", length = 600, nullable = false)
    private String contenidoQr;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoQr tipo; // PAGO o VIAJE

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;


    @Column(name = "consumido", nullable = false)
    private Boolean consumido = false;

    @OneToOne(mappedBy = "qr")
    private PasajeroViaje pasajeroViaje;

    @ManyToOne
    @JoinColumn(name = "id_pagoFK")
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "id_viajeFK")
    private Viaje viaje;

    // Usuario dueño del QR
    @ManyToOne
    @JoinColumn(name = "id_usuarioFK", nullable = false)
    private Usuario usuario;


}
