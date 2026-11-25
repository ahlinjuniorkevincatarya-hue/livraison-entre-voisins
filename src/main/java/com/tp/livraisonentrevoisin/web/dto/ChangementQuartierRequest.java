package com.tp.livraisonentrevoisin.web.dto;

import jakarta.validation.constraints.NotNull;

public record ChangementQuartierRequest(@NotNull Long quartierId) { }
