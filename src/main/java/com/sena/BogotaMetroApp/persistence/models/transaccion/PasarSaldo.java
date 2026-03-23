package com.sena.BogotaMetroApp.persistence.models.transaccion;

import com.sena.BogotaMetroApp.persistence.models.TarjetaVirtual;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="pasar_saldo")
@PrimaryKeyJoinColumn(name = "id_transaccion")
@Getter
@Setter
public class PasarSaldo extends Transaccion {
    @ManyToOne
    @JoinColumn(name = "id_tarjeta_origen_fk", nullable = false)
    private TarjetaVirtual tarjetaOrigen;
    @ManyToOne
    @JoinColumn(name = "id_tarjeta_destino_fk", nullable = false)
    private TarjetaVirtual tarjetaDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_de_pago", nullable = false)
    private MedioPagoEnum medioDePago;

    @Override
    public MedioPagoEnum obtenerMedioDePago() {
        return medioDePago;
    }
}
