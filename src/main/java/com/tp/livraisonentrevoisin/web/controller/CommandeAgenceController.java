package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.CommandeGroupeeService;
import com.tp.livraisonentrevoisin.service.facade.LivraisonFacade;
import com.tp.livraisonentrevoisin.web.dto.ConfirmerCommandeRequest;
import com.tp.livraisonentrevoisin.web.dto.GroupeReponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// API de l'agence pour les commandes groupées (partie B, F11)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agences/{agenceId}/commandes")
public class CommandeAgenceController {

    private final LivraisonFacade facade;                       // Facade de Kevin (on ne la modifie pas)
    private final CommandeGroupeeService commandeGroupeeService; // votre service

    // F11 - les commandes reçues : GET /api/agences/7/commandes
    @GetMapping
    public List<GroupeReponse> lister(@PathVariable Long agenceId) {
        return commandeGroupeeService.listerCommandesAgence(agenceId).stream()
                .map(GroupeReponse::from)
                .toList();
    }

    // F11 - confirmer et fixer les frais : PATCH /api/agences/7/commandes/2/confirmation
    @PatchMapping("/{groupeId}/confirmation")
    public GroupeReponse confirmer(@PathVariable Long agenceId,
                                   @PathVariable Long groupeId,
                                   @Valid @RequestBody ConfirmerCommandeRequest request) {
        return GroupeReponse.from(facade.confirmer(groupeId, agenceId, request.frais()));
    }
}