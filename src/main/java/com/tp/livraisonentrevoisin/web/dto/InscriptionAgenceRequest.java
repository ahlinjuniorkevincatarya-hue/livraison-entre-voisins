package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record InscriptionAgenceRequest(
        @NotBlank String nom,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "6 caractères minimum") String motDePasse,
        @NotBlank String nomAgence,
        @NotEmpty List<Long> quartierIds) {
}
