package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.CommandeGroupeeService;
import com.tp.livraisonentrevoisin.service.facade.LivraisonFacade;
import com.tp.livraisonentrevoisin.web.dto.GroupeReponse;
import com.tp.livraisonentrevoisin.web.dto.OuvrirGroupeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// API des voisins pour les groupes (partie B)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groupes")
public class GroupeController {

    private final LivraisonFacade facade;                       // Facade de Kevin (on ne la modifie pas)
    private final CommandeGroupeeService commandeGroupeeService; // votre service

    // F6 - ouvrir un groupe : POST /api/groupes
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupeReponse ouvrir(@Valid @RequestBody OuvrirGroupeRequest request) {
        return GroupeReponse.from(facade.ouvrirGroupe(
                request.organisateurId(),
                request.agenceId(),
                request.heureLimite(),
                request.pointRetrait()));
    }

    // F7 - les groupes ouverts du quartier : GET /api/groupes?voisinId=3
    @GetMapping
    public List<GroupeReponse> listerOuverts(@RequestParam Long voisinId) {
        return facade.listerGroupesOuverts(voisinId).stream()
                .map(GroupeReponse::from)
                .toList();
    }

    // détail d'un groupe : GET /api/groupes/1
    @GetMapping("/{groupeId}")
    public GroupeReponse detail(@PathVariable Long groupeId) {
        return GroupeReponse.from(commandeGroupeeService.trouver(groupeId));
    }

    // F9 - clôturer : PATCH /api/groupes/1/cloture?voisinId=2
    @PatchMapping("/{groupeId}/cloture")
    public GroupeReponse cloturer(@PathVariable Long groupeId, @RequestParam Long voisinId) {
        return GroupeReponse.from(facade.cloturer(groupeId, voisinId));
    }

    // F10 - annuler : PATCH /api/groupes/1/annulation?voisinId=2
    @PatchMapping("/{groupeId}/annulation")
    public GroupeReponse annuler(@PathVariable Long groupeId, @RequestParam Long voisinId) {
        return GroupeReponse.from(facade.annuler(groupeId, voisinId));
    }
}