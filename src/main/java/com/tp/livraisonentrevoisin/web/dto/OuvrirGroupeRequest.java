package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

// F6 - les données envoyées pour ouvrir un groupe
public record OuvrirGroupeRequest(
        @NotNull Long organisateurId,
        @NotNull Long agenceId,
        @NotNull @Future LocalDateTime heureLimite,
        @NotBlank String pointRetrait) {
}