package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.Participation;

import java.util.List;

// Ce que l'API renvoie pour une participation
public record ParticipationReponse(
        Long id,
        Long groupeId,
        Long voisinId,
        String nomVoisin,
        String lieuLivraison,
        boolean estRegle,
        List<LigneArticleReponse> lignes) {

    public static ParticipationReponse from(Participation p) {
        return new ParticipationReponse(
                p.getId(),
                p.getCommande().getId(),
                p.getVoisin().getId(),
                p.getVoisin().getNom(),
                p.getLieuLivraison(),
                p.isEstRegle(),
                p.getLignes().stream().map(LigneArticleReponse::from).toList());
    }
}