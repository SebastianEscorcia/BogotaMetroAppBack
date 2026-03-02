package com.sena.BogotaMetroApp.persistence.models.transaccion;

import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="cobros_pasajes")
@PrimaryKeyJoinColumn(name = "id_transaccion")
@Getter
@Setter
public class CobroPasaje extends Transaccion {
    @Column(name="estacion_id")
    private Long estacionId;

    @Override
    public MedioPagoEnum obtenerMedioDePago() {
        return MedioPagoEnum.PASAJES;
    }
}
