package com.sena.BogotaMetroApp.persistence.models.qr;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
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
    private TipoQr tipo;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;


    @Column(name = "consumido", nullable = false)
    private Boolean consumido = false;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    // Usuario dueño del QR
    @ManyToOne
    @JoinColumn(name = "id_usuarioFK", nullable = false)
    private Usuario usuario;

    @Version
    private Long version;



}
