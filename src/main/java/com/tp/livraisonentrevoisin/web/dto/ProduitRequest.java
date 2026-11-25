package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProduitRequest(
        @NotBlank String nom,
        @Positive double prix
) {
}
