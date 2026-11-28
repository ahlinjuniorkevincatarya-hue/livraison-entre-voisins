package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.Min;

// F8 - données envoyées pour changer la quantité d'un article
public record ModifierArticleRequest(@Min(1) int quantite) {
}