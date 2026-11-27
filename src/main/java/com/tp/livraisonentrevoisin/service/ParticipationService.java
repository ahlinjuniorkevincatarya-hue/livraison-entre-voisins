package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.*;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.*;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
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
    private final CommandeRepository commandeRepository;
    private final VoisinRepository voisinRepository;

    // ---------- F7 : rejoindre un groupe ----------
    @Transactional
    public Participation rejoindre(Long groupeId, Long voisinId) {
        CommandeGroupee commande = commandeRepository.findById(groupeId)
                .orElseThrow(() -> new RegleMetierException("Groupe introuvable"));
        Voisin voisin = voisinRepository.findById(voisinId)
                .orElseThrow(() -> new RegleMetierException("Voisin introuvable"));

        if (!voisin.isActif()) {                                                          // RG12
            throw new RegleMetierException("Votre compte est bloqué");
        }
        verifierGroupeModifiable(commande);                                               // ouvert + heure limite
        if (!voisin.getQuartier().getId().equals(commande.getOrganisateur().getQuartier().getId())) {
            throw new RegleMetierException("Ce groupe n'est pas dans votre quartier");    // RG1
        }
        if (participationRepository.findByCommandeIdAndVoisinId(groupeId, voisinId).isPresent()) {
            throw new RegleMetierException("Vous participez déjà à ce groupe");           // RG4
        }

        Participation participation = new Participation();
        participation.setCommande(commande);
        participation.setVoisin(voisin);
        commande.getParticipations().add(participation);
        return participationRepository.save(participation);
    }

    // ---------- Donner le lieu de livraison ----------
    @Transactional
    public Participation definirLieuLivraison(Long participationId, Long voisinId, String lieu) {
        Participation participation = participationExistante(participationId);
        if (!participation.getVoisin().getId().equals(voisinId)) {
            throw new RegleMetierException("Cette participation n'est pas la vôtre");
        }
        if (lieu == null || lieu.isBlank()) {
            throw new RegleMetierException("Le lieu de livraison est obligatoire");
        }
        StatutCommande statut = participation.getCommande().getStatut();
        if (statut != StatutCommande.OUVERTE && statut != StatutCommande.CLOTUREE
                && statut != StatutCommande.CONFIRMEE) {
            throw new RegleMetierException("Le lieu de livraison ne peut plus être modifié");
        }
        participation.setLieuLivraison(lieu.trim());
        return participationRepository.save(participation);
    }

    // ---------- Retrouver sa participation dans un groupe ----------
    @Transactional(readOnly = true)
    public Participation consulterParticipation(Long groupeId, Long voisinId) {
        return participationRepository.findByCommandeIdAndVoisinId(groupeId, voisinId)
                .orElseThrow(() -> new RegleMetierException("Vous ne participez pas à ce groupe"));
    }

    // ---------- F8 : ajouter un article ----------
    @Transactional
    public LigneArticle ajouterArticle(Long participationId, Long produitId, int quantite) {
        Participation participation = participationExistante(participationId);
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RegleMetierException("Produit introuvable"));

        CommandeGroupee commande = participation.getCommande();
        verifierGroupeModifiable(commande);
        verifierQuantite(quantite);

        if (!produit.isActif()) {
            throw new RegleMetierException("Ce produit n'est plus disponible");
        }
        if (!produit.getAgence().getId().equals(commande.getAgence().getId())) {
            throw new RegleMetierException("Ce produit n'appartient pas à l'agence du groupe");
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
        ligne.setPrixUnitaire(produit.getPrix());       // prix figé au moment de l'ajout
        participation.getLignes().add(ligne);
        return articleRepository.save(ligne);
    }

    // ---------- F8 : modifier la quantité d'un article ----------
    @Transactional
    public LigneArticle modifierArticle(Long ligneId, int nouvelleQuantite) {
        LigneArticle ligne = articleRepository.findById(ligneId)
                .orElseThrow(() -> new RegleMetierException("Article introuvable"));

        verifierGroupeModifiable(ligne.getParticipation().getCommande());
        verifierQuantite(nouvelleQuantite);

        ligne.setQuantite(nouvelleQuantite);
        return articleRepository.save(ligne);
    }

    // ---------- F8 : retirer un article ----------
    @Transactional
    public void retirerArticle(Long ligneId) {
        LigneArticle ligne = articleRepository.findById(ligneId)
                .orElseThrow(() -> new RegleMetierException("Article introuvable"));

        verifierGroupeModifiable(ligne.getParticipation().getCommande());

        ligne.getParticipation().getLignes().remove(ligne);
        articleRepository.delete(ligne);
    }

    // ---------- règles communes ----------
    private void verifierGroupeModifiable(CommandeGroupee commande) {
        if (commande.getStatut() != StatutCommande.OUVERTE) {
            throw new RegleMetierException("Le groupe n'est plus ouvert");
        }
        if (!LocalDateTime.now().isBefore(commande.getHeureLimite())) {
            throw new RegleMetierException("L'heure limite est dépassée");
        }
    }

    private void verifierQuantite(int quantite) {
        if (quantite < 1) {
            throw new RegleMetierException("La quantité doit être supérieure ou égale à 1");
        }
    }

    private Participation participationExistante(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new RegleMetierException("Participation introuvable"));
    }
}