package com.tp.livraisonentrevoisin.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class LigneArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantite;

    private double prixUnitaire;

    @ManyToOne(optional = false)
    private Produit produit;

    @ManyToOne(optional = false)
    private Participation participation;
}
