package com.sena.BogotaMetroApp.persistence.models.transaccion;

import com.sena.BogotaMetroApp.persistence.models.pasarela.Pasarela;
import com.sena.BogotaMetroApp.utils.enums.MedioPagoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recargas")
@PrimaryKeyJoinColumn(name = "id_transaccion") // Usa el mismo ID del padre
@Getter
@Setter
public class Recarga extends Transaccion {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pasarela_fk", nullable = false) // Aquí SÍ es obligatorio
    private Pasarela pasarela;

    @Column(name = "referencia_pasarela", nullable = false)
    private String referenciaPasarela;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_de_pago", nullable = false)
    private MedioPagoEnum medioDePago;

}
