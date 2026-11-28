package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.facade.LivraisonFacade;
import com.tp.livraisonentrevoisin.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// API des voisins pour la participation, les articles, les parts et le règlement (partie C)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipationController {

    private final LivraisonFacade facade;

    // F7 - rejoindre un groupe : POST /api/groupes/1/participations?voisinId=3
    @PostMapping("/groupes/{groupeId}/participations")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationReponse rejoindre(@PathVariable Long groupeId, @RequestParam Long voisinId) {
        return ParticipationReponse.from(facade.rejoindre(groupeId, voisinId));
    }

    // lieu de livraison : PATCH /api/participations/2/lieu-livraison?voisinId=3
    @PatchMapping("/participations/{participationId}/lieu-livraison")
    public ParticipationReponse definirLieu(@PathVariable Long participationId,
                                            @RequestParam Long voisinId,
                                            @Valid @RequestBody LieuLivraisonRequest request) {
        return ParticipationReponse.from(
                facade.definirLieuLivraison(participationId, voisinId, request.lieu()));
    }

    // F8 - ajouter un article : POST /api/participations/1/articles
    @PostMapping("/participations/{participationId}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public LigneArticleReponse ajouterArticle(@PathVariable Long participationId,
                                              @Valid @RequestBody AjouterArticleRequest request) {
        return LigneArticleReponse.from(
                facade.ajouterArticle(participationId, request.produitId(), request.quantite()));
    }

    // F8 - modifier la quantité : PATCH /api/articles/1
    @PatchMapping("/articles/{ligneId}")
    public LigneArticleReponse modifierArticle(@PathVariable Long ligneId,
                                               @Valid @RequestBody ModifierArticleRequest request) {
        return LigneArticleReponse.from(facade.modifierArticle(ligneId, request.quantite()));
    }

    // F8 - retirer un article : DELETE /api/articles/1
    @DeleteMapping("/articles/{ligneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void retirerArticle(@PathVariable Long ligneId) {
        facade.retirerArticle(ligneId);
    }

    // F12 - ma part : GET /api/groupes/3/parts/moi?voisinId=2
    @GetMapping("/groupes/{groupeId}/parts/moi")
    public Map<String, Double> maPart(@PathVariable Long groupeId, @RequestParam Long voisinId) {
        return Map.of("part", facade.consulterMaPart(groupeId, voisinId));
    }

    // F12 - la part de chaque participant : GET /api/groupes/3/parts
    @GetMapping("/groupes/{groupeId}/parts")
    public Map<Long, Double> parts(@PathVariable Long groupeId) {
        return facade.calculerParts(groupeId);
    }

    // F13 - marquer la commande livrée : PATCH /api/groupes/3/livraison?organisateurId=2
    @PatchMapping("/groupes/{groupeId}/livraison")
    public GroupeReponse livrer(@PathVariable Long groupeId, @RequestParam Long organisateurId) {
        return GroupeReponse.from(facade.livrer(groupeId, organisateurId));
    }

    // F13 - marquer une part réglée : PATCH /api/participations/9/reglement?organisateurId=5
    @PatchMapping("/participations/{participationId}/reglement")
    public ParticipationReponse regler(@PathVariable Long participationId,
                                       @RequestParam Long organisateurId) {
        return ParticipationReponse.from(facade.regler(participationId, organisateurId));
    }
}