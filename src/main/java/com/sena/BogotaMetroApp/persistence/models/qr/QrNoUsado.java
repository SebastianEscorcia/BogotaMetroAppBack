package com.sena.BogotaMetroApp.persistence.models.qr;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.utils.enums.TipoQr;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "qrs_no_usados")
@Getter
@Setter
public class QrNoUsado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenidoQr;


    private LocalDateTime fechaGeneracion;

    private LocalDateTime fechaExpiracion;

    private LocalDateTime fechaMovimiento; // cuándo fue movido a esta tabla

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoQr tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @PrePersist
    private void validate() {
        if (tipo == null) {
            throw new IllegalStateException("El tipo del QR no puede ser nulo antes de guardar.");
        }
    }

}
