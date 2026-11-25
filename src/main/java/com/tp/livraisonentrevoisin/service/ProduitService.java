package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Produit;
import com.tp.livraisonentrevoisin.dao.repositories.AgenceRepository;
import com.tp.livraisonentrevoisin.dao.repositories.ProduitRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final AgenceRepository agenceRepository;

    // F4 : Ajouter un produit à une agence
    public Produit ajouter(Long agenceId, String nom, double prix) {
        AgenceLivraison agence = agenceExistante(agenceId);
        if(!agence.isValide()){
            throw new RegleMetierException("Votre agence n'a pas encore été validée par l'administrateur");
        }
        verifier(nom, prix);

        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setPrix(prix);
        produit.setAgence(agence);
        return produitRepository.save(produit);
    }

    //F4 : Modifier un produit d'une agence
    public Produit modifier(Long agenceId, Long produitId, String nom, double prix) {
        Produit produit = produitDeLAgence(agenceId, produitId);
        verifier(nom, prix);

        produit.setNom(nom);
        produit.setPrix(prix);
        return produitRepository.save(produit);
    }

    // F4 : Désactiver un produit d'une agence
    public void desactiver(Long agenceId, Long produitId) {
        Produit produit = produitDeLAgence(agenceId, produitId);
        produit.setActif(false);
        produitRepository.save(produit);
    }

    public void reactiver(Long agenceId, Long produitId) {
        Produit produit = produitDeLAgence(agenceId, produitId);
        produit.setActif(true);
        produitRepository.save(produit);
    }

    //F4 : l'agence voit tous ses produits, actifs ou non
    @Transactional(readOnly = true)
    public List<Produit> lister(Long agenceId) {
        agenceExistante(agenceId);
        return produitRepository.findByAgenceId(agenceId);
    }

    //F5 : un voisin consulte le catalogue d'une agence (uniquement les produits actifs)
    @Transactional(readOnly = true)
    public List<Produit> consulterCatalogue(Long agenceId) {
        AgenceLivraison agence = agenceExistante(agenceId);
        if(!agence.isValide()){
            throw new RegleMetierException("Cette agence n'a pas encore été validée par l'administrateur");
        }
        return produitRepository.findByAgenceIdAndActifTrue(agenceId);
    }

    // Méthodes privées

    private AgenceLivraison agenceExistante(Long agenceId) {
        return agenceRepository.findById(agenceId).orElseThrow(() -> new RegleMetierException("Agence introuvable"));
    }

    private Produit produitDeLAgence(Long agenceId, Long produitId) {
        Produit produit = produitRepository.findById(produitId).orElseThrow(() -> new RegleMetierException("Produit introuvable"));

        if(!produit.getAgence().getId().equals(agenceId)) {
            throw new RegleMetierException("Le produit n'appartient pas à l'agence spécifiée");
        }
        return produit;
    }

    private void verifier(String nom, double prix) {
        if(nom == null || nom.isBlank()) {
            throw new RegleMetierException("Le nom du produit est obligatoire");
        }
        if(prix <= 0) {
            throw new RegleMetierException("Le prix du produit doit être supérieur à zéro");
        }
    }
}
