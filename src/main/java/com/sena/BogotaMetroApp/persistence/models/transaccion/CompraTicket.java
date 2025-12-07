package com.sena.BogotaMetroApp.persistence.models.transaccion;

import com.sena.BogotaMetroApp.persistence.models.pasajeroviaje.PasajeroViaje;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "compras_tickets")
@PrimaryKeyJoinColumn(name = "id_transaccion")
@Getter
@Setter
public class CompraTicket extends Transaccion {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket_fk", referencedColumnName = "id_ticket", nullable = false)
    private PasajeroViaje ticket;
}
