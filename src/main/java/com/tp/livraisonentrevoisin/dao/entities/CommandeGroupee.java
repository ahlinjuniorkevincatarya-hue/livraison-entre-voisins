package com.tp.livraisonentrevoisin.dao.entities;

import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class CommandeGroupee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCommande statut = StatutCommande.OUVERTE;

    @Column(nullable = false)
    private LocalDateTime heureLimite;

    private String pointRetrait ;

    private Double fraisLivraison;

    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(optional = false)
    private AgenceLivraison agence;

    @ManyToOne(optional = false)
    private Voisin organisateur;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();
}
