package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;

// Ce que l'API renvoie pour une ligne d'article
public record LigneArticleReponse(
        Long id,
        Long produitId,
        String nomProduit,
        int quantite,
        double prixUnitaire,
        double montant) {

    public static LigneArticleReponse from(LigneArticle ligne) {
        return new LigneArticleReponse(
                ligne.getId(),
                ligne.getProduit().getId(),
                ligne.getProduit().getNom(),
                ligne.getQuantite(),
                ligne.getPrixUnitaire(),
                ligne.getPrixUnitaire() * ligne.getQuantite());
    }
}