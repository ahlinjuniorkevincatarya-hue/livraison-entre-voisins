package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.QuartierService;
import com.tp.livraisonentrevoisin.web.dto.AgenceReponse;
import com.tp.livraisonentrevoisin.web.dto.ChangementQuartierRequest;
import com.tp.livraisonentrevoisin.web.dto.QuartierReponse;
import com.tp.livraisonentrevoisin.web.dto.UtilisateurReponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuartierController {

    private final QuartierService quartierService;

    @GetMapping("/api/quartiers")
    public List<QuartierReponse> lister() {
        return quartierService.listerQuartiers().stream().map(QuartierReponse::from).toList();
    }

    @PatchMapping("/api/voisins/{voisinId}/quartier")
    public UtilisateurReponse changerQuartier(@PathVariable Long voisinId,
                                              @Valid @RequestBody ChangementQuartierRequest r) {
        return UtilisateurReponse.from(quartierService.changerQuartier(voisinId, r.quartierId()));
    }

    @PostMapping("/api/agences/{agenceId}/quartiers/{quartierId}")
    public AgenceReponse desservir(@PathVariable Long agenceId, @PathVariable Long quartierId) {
        return AgenceReponse.from(quartierService.desservir(agenceId, quartierId));
    }

    @DeleteMapping("/api/agences/{agenceId}/quartiers/{quartierId}")
    public AgenceReponse cesserDeDesservir(@PathVariable Long agenceId, @PathVariable Long quartierId) {
        return AgenceReponse.from(quartierService.cesserDeDesservir(agenceId, quartierId));
    }
}