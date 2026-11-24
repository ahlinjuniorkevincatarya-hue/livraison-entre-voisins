package com.tp.livraisonentrevoisin.dao.entities;

import com.tp.livraisonentrevoisin.dao.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class AgenceLivraison extends Utilisateur{
    @Column(nullable = false)
    private String nomAgence;

    private boolean valide = false; //Valide par l'administrateur

    @ManyToMany
    @JoinTable(name = "agence_quartier",
             joinColumns = @JoinColumn(name = "agence_id"),
             inverseJoinColumns = @JoinColumn(name = "quartier_id"))
    private Set<Quartier> quartiers = new HashSet<>();

    public AgenceLivraison() {
        setRole(Role.AGENCE);
    }
}
