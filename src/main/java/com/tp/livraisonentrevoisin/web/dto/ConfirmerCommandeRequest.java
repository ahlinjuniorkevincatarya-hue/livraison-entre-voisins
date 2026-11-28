package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// F11 - les données envoyées par l'agence pour confirmer une commande
public record ConfirmerCommandeRequest(
        @NotNull @PositiveOrZero Double frais) {
}