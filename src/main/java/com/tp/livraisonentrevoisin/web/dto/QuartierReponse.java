package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.Quartier;

public record QuartierReponse(Long id, String nom, String ville) {
    public static QuartierReponse from(Quartier q) {
        return new QuartierReponse(q.getId(), q.getNom(), q.getVille());
    }
}
