package com.sena.BogotaMetroApp.persistence.models.pasajeroviaje;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sena.BogotaMetroApp.persistence.models.Viaje;
import com.sena.BogotaMetroApp.persistence.models.pasajero.Pasajero;
import com.sena.BogotaMetroApp.persistence.models.qr.Qr;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pasajeros_viajes")
@Getter
@Setter
@NoArgsConstructor
public class PasajeroViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasajero")
    private Pasajero pasajero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viaje")
    private Viaje viaje;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_qrFK", nullable = false, unique = true)
    @JsonIgnore
    private Qr qr;
}
