package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.Produit;

public record ProduitReponse(Long id, String nom, double prix, boolean actif) {
    public static ProduitReponse from(Produit produit) {
        return new ProduitReponse(produit.getId(), produit.getNom(), produit.getPrix(), produit.isActif());
    }
}
