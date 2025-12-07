package com.sena.BogotaMetroApp.persistence.models;

import com.sena.BogotaMetroApp.persistence.models.sesionchat.SesionChat;

import com.sena.BogotaMetroApp.utils.enums.TipoRemitenteEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes_chat")
@Getter
@Setter
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name="tipo_remitente", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoRemitenteEnum tipoRemitente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_remitente", nullable = false)
    private Usuario remitente;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_soporte_pasajero_fk")
    private SesionChat sesionChat;
}
