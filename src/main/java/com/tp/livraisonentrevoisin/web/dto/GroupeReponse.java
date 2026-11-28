package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;

import java.time.LocalDateTime;

// Ce que l'API renvoie pour un groupe (commande groupée)
public record GroupeReponse(
        Long id,
        StatutCommande statut,
        LocalDateTime heureLimite,
        String pointRetrait,
        Double fraisLivraison,
        Long agenceId,
        String nomAgence,
        Long organisateurId,
        String nomOrganisateur,
        int nombreParticipants) {

    public static GroupeReponse from(CommandeGroupee commande) {
        return new GroupeReponse(
                commande.getId(),
                commande.getStatut(),
                commande.getHeureLimite(),
                commande.getPointRetrait(),
                commande.getFraisLivraison(),
                commande.getAgence().getId(),
                commande.getAgence().getNomAgence(),
                commande.getOrganisateur().getId(),
                commande.getOrganisateur().getNom(),
                commande.getParticipations().size());
    }
}