package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.AdministrationService;
import com.tp.livraisonentrevoisin.web.dto.AgenceReponse;
import com.tp.livraisonentrevoisin.web.dto.QuartierReponse;
import com.tp.livraisonentrevoisin.web.dto.QuartierRequest;
import com.tp.livraisonentrevoisin.web.dto.UtilisateurReponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdministrationService administrationService;

    // adminId en paramètre : provisoire, il viendra de l'utilisateur connecté

    @GetMapping("/agences/a-valider")
    public List<AgenceReponse> agencesAValider(@RequestParam Long adminId) {
        return administrationService.listerAgencesAValider(adminId).stream().map(AgenceReponse::from).toList();
    }

    @PatchMapping("/agences/{agenceId}/validation")
    public AgenceReponse valider(@RequestParam Long adminId, @PathVariable Long agenceId) {
        return AgenceReponse.from(administrationService.validerAgence(adminId, agenceId));
    }

    @PatchMapping("/utilisateurs/{id}/blocage")
    public UtilisateurReponse bloquer(@RequestParam Long adminId, @PathVariable Long id) {
        return UtilisateurReponse.from(administrationService.bloquer(adminId, id));
    }

    @PatchMapping("/utilisateurs/{id}/deblocage")
    public UtilisateurReponse debloquer(@RequestParam Long adminId, @PathVariable Long id) {
        return UtilisateurReponse.from(administrationService.debloquer(adminId, id));
    }

    @PostMapping("/quartiers")
    @ResponseStatus(HttpStatus.CREATED)
    public QuartierReponse creerQuartier(@RequestParam Long adminId, @Valid @RequestBody QuartierRequest r) {
        return QuartierReponse.from(administrationService.creerQuartier(adminId, r.nom(), r.ville()));
    }

    @PutMapping("/quartiers/{id}")
    public QuartierReponse modifierQuartier(@RequestParam Long adminId, @PathVariable Long id,
                                            @Valid @RequestBody QuartierRequest r) {
        return QuartierReponse.from(administrationService.modifierQuartier(adminId, id, r.nom(), r.ville()));
    }
}
