package com.tp.livraisonentrevoisin.web.dto;

import com.tp.livraisonentrevoisin.dao.entities.Utilisateur;
import com.tp.livraisonentrevoisin.dao.enums.Role;

public record UtilisateurReponse(Long id, String nom, String email, Role role, boolean actif) {
    public static UtilisateurReponse from(Utilisateur u) {
        return new UtilisateurReponse(u.getId(), u.getNom(), u.getEmail(), u.getRole(), u.isActif());
    }
}