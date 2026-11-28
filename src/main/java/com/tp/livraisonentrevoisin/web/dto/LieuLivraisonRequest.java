package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.NotBlank;

// données envoyées pour indiquer son lieu de livraison
public record LieuLivraisonRequest(@NotBlank String lieu) {
}