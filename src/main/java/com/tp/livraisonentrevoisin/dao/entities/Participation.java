package com.tp.livraisonentrevoisin.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"commande_id", "voisin_id"}))
@Getter @Setter @NoArgsConstructor
public class Participation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean estRegle = false;

    private String lieuLivraison;

    @ManyToOne(optional = false)
    private CommandeGroupee commande;

    @ManyToOne(optional = false)
    private Voisin voisin;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL)
    private List<LigneArticle> lignes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Paiement paiement;

}
