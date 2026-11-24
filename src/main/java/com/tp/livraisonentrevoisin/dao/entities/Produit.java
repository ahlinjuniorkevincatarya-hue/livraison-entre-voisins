package com.tp.livraisonentrevoisin.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private double prix;

    private boolean actif = true;

    @ManyToOne(optional = false)
    private AgenceLivraison agence;
}
