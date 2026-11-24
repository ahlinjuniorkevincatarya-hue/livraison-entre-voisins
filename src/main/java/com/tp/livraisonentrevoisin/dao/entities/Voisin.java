package com.tp.livraisonentrevoisin.dao.entities;

import com.tp.livraisonentrevoisin.dao.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Voisin extends Utilisateur {
    private String adresse;

    @ManyToOne(optional = false)
    private Quartier quartier;

    public Voisin() {
        setRole(Role.VOISIN);
    }
}
