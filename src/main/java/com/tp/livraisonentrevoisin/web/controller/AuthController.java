package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.UtilisateurService;
import com.tp.livraisonentrevoisin.web.dto.InscriptionAgenceRequest;
import com.tp.livraisonentrevoisin.web.dto.InscriptionVoisinRequest;
import com.tp.livraisonentrevoisin.web.dto.UtilisateurReponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService utilisateurService;

    @PostMapping("/inscription/voisin")
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurReponse inscrireVoisin(@Valid @RequestBody InscriptionVoisinRequest request){
        return UtilisateurReponse.from(utilisateurService.inscrireVoisin(
                request.nom(),
                request.email(),
                request.motDePasse(),
                request.adresse(),
                request.quartierId()
        ));
    }

    @PostMapping("/inscription/agence")
    @ResponseStatus(HttpStatus.CREATED)
    public UtilisateurReponse inscrireAgence(@Valid @RequestBody InscriptionAgenceRequest request){
        return UtilisateurReponse.from(utilisateurService.inscrireAgence(
                request.nom(),
                request.email(),
                request.motDePasse(),
                request.nomAgence(),
                request.quartierIds()
        ));
    }
}
