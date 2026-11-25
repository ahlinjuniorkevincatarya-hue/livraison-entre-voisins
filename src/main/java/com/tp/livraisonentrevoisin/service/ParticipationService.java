package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.entities.Produit;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.ArticleRepository;
import com.tp.livraisonentrevoisin.dao.repositories.ParticipationRepository;
import com.tp.livraisonentrevoisin.dao.repositories.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ArticleRepository articleRepository;
    private final ProduitRepository produitRepository;

    // F8 - ajouter un article
    @Transactional
    public LigneArticle ajouterArticle(Long participationId, Long produitId, int quantite) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("Participation introuvable"));
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable"));

        CommandeGroupee commande = participation.getCommande();
        verifierGroupeModifiable(commande);
        verifierQuantite(quantite);

        if (!produit.isActif()) {
            throw new IllegalStateException("Ce produit n'est plus disponible");
        }
        if (!produit.getAgence().getId().equals(commande.getAgence().getId())) {
            throw new IllegalStateException("Ce produit n'appartient pas à l'agence du groupe");
        }

        // Si le produit est déjà dans la participation, on augmente la quantité
        for (LigneArticle existante : participation.getLignes()) {
            if (existante.getProduit().getId().equals(produitId)) {
                existante.setQuantite(existante.getQuantite() + quantite);
                return articleRepository.save(existante);
            }
        }

        LigneArticle ligne = new LigneArticle();
        ligne.setParticipation(participation);
        ligne.setProduit(produit);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(produit.getPrix()); // prix figé au moment de l'ajout
        participation.getLignes().add(ligne);
        return articleRepository.save(ligne);
    }

    // F8 - modifier la quantité d'un article
    @Transactional
    public LigneArticle modifierArticle(Long ligneId, int nouvelleQuantite) {
        LigneArticle ligne = articleRepository.findById(ligneId)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));

        verifierGroupeModifiable(ligne.getParticipation().getCommande());
        verifierQuantite(nouvelleQuantite);

        ligne.setQuantite(nouvelleQuantite);
        return articleRepository.save(ligne);
    }

    // F8 - retirer un article
    @Transactional
    public void retirerArticle(Long ligneId) {
        LigneArticle ligne = articleRepository.findById(ligneId)
                .orElseThrow(() -> new IllegalArgumentException("Article introuvable"));

        verifierGroupeModifiable(ligne.getParticipation().getCommande());

        ligne.getParticipation().getLignes().remove(ligne);
        articleRepository.delete(ligne);
    }

    // ----- règles communes -----

    private void verifierGroupeModifiable(CommandeGroupee commande) {
        if (commande.getStatut() != StatutCommande.OUVERTE) {
            throw new IllegalStateException("Le groupe n'est plus ouvert");
        }
        if (!LocalDateTime.now().isBefore(commande.getHeureLimite())) {
            throw new IllegalStateException("L'heure limite est dépassée");
        }
    }

    private void verifierQuantite(int quantite) {
        if (quantite < 1) {
            throw new IllegalArgumentException("La quantité doit être supérieure ou égale à 1");
        }
    }
}