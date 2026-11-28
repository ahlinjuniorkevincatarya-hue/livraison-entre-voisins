package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// F8 - données envoyées pour ajouter un article
public record AjouterArticleRequest(
        @NotNull Long produitId,
        @Min(1) int quantite) {
}