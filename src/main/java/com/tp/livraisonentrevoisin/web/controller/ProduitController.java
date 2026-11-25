package com.tp.livraisonentrevoisin.web.controller;

import com.tp.livraisonentrevoisin.service.ProduitService;
import com.tp.livraisonentrevoisin.web.dto.ProduitReponse;
import com.tp.livraisonentrevoisin.web.dto.ProduitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agences/{agenceId}")
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping("/produits")
    @ResponseStatus(HttpStatus.CREATED)
    public ProduitReponse ajouter(@PathVariable Long agenceId, @Valid @RequestBody ProduitRequest request){
        return ProduitReponse.from(produitService.ajouter(agenceId, request.nom(), request.prix()));
    }

    @PutMapping("/produits/{produitId}")
    public ProduitReponse modifier(@PathVariable Long agenceId, @PathVariable Long produitId, @Valid @RequestBody ProduitRequest request){
        return ProduitReponse.from(produitService.modifier(agenceId, produitId, request.nom(), request.prix()));
    }

    @PatchMapping("/produits/{produitId}/desactivation")
    public void desactiver(@PathVariable Long agenceId, @PathVariable Long produitId){
        produitService.desactiver(agenceId, produitId);
    }

    @PatchMapping("/produits/{produitId}/activation")
    public void reactiver(@PathVariable Long agenceId, @PathVariable Long produitId){
        produitService.reactiver(agenceId, produitId);
    }

    @GetMapping("/produits")
    public List<ProduitReponse> lister(@PathVariable Long agenceId){
        return produitService.lister(agenceId).stream()
                .map(ProduitReponse::from)
                .toList();
    }

    // F% : un voisin consulte le catalogue d'une agence
    @GetMapping("/catalogue")
    public List<ProduitReponse> consulterCatalogue(@PathVariable Long agenceId){
        return produitService.consulterCatalogue(agenceId).stream()
                .map(ProduitReponse::from)
                .toList();
    }
}
