package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InscriptionVoisinRequest(
        @NotBlank String nom,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "6 caractères minimum") String motDePasse,
        @NotBlank String adresse,
        @NotNull Long quartierId) {
}
