package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;

public record AgenceReponse(Long id, String nom, String nomAgence, boolean valide, boolean actif) {
    public static AgenceReponse from(AgenceLivraison a) {
        return new AgenceReponse(a.getId(), a.getNom(), a.getNomAgence(), a.isValide(), a.isActif());
    }
}
