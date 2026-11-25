package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.NotBlank;

public record QuartierRequest(
        @NotBlank String nom,
        @NotBlank String ville
) {
}
