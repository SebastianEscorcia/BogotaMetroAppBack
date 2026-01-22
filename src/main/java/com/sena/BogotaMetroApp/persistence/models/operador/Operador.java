package com.sena.BogotaMetroApp.persistence.models.operador;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "operadores")
@Getter
@Setter
public class Operador implements Persistable<Long> {

    @Id
    @Column(name = "id_usuario")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    //Campo auxiliar para controlar el estado (no se guarda en BD)
    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    //  Después de cargar de BD o antes de persistir, ya no es nuevo
    @PostLoad
    @PrePersist
    void markNotNew() {
        this.isNew = false;
    }
}
