package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.entities.Produit;
import com.tp.livraisonentrevoisin.dao.repositories.ArticleRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProduitServiceTest {

    @Autowired
    ProduitService produitService;
    @Autowired
    ArticleRepository ligneArticleRepository;

    @Test
    void ajouter_creeUnProduitActif() {
        Produit p = produitService.ajouter(7L, "Sucre 1kg", 8.5);
        assertThat(p.getId()).isNotNull();
        assertThat(p.isActif()).isTrue();
        assertThat(p.getAgence().getId()).isEqualTo(7L);
    }

    @Test
    void ajouter_refuseUneAgenceNonValidee() {
        assertThatThrownBy(() -> produitService.ajouter(8L, "Sucre", 8.5))
                .isInstanceOf(RegleMetierException.class)
                .hasMessageContaining("validée");
    }

    @Test
    void ajouter_refuseUnPrixNegatifOuNul() {
        assertThatThrownBy(() -> produitService.ajouter(7L, "Sucre", 0))
                .isInstanceOf(RegleMetierException.class);
        assertThatThrownBy(() -> produitService.ajouter(7L, "Sucre", -3))
                .isInstanceOf(RegleMetierException.class);
    }

    @Test
    void modifier_refuseLeProduitDUneAutreAgence() {
        // le produit 7 (Thé) appartient à l'agence 8, pas à la 7.
        assertThatThrownBy(() -> produitService.modifier(7L, 7L, "Thé", 25))
                .isInstanceOf(RegleMetierException.class)
                .hasMessageContaining("n'appartient pas");
    }

    @Test
    void modifier_neChangePasLHistoriqueDesLignes() {
        produitService.modifier(7L, 1L, "Lait 1L", 6.0);   // le lait passe de 5 à 6 DH

        LigneArticle ligne = ligneArticleRepository.findById(1L).orElseThrow();
        assertThat(ligne.getPrixUnitaire()).isEqualTo(5.0);  // l'ancienne commande garde son prix
    }

    @Test
    void desactiver_retireDuCatalogueMaisGardeLHistorique() {
        assertThat(produitService.consulterCatalogue(7L)).hasSize(5);   // le fromage est déjà inactif

        produitService.desactiver(7L, 1L);                               // on désactive le lait

        assertThat(produitService.consulterCatalogue(7L)).hasSize(4);
        assertThat(ligneArticleRepository.findById(1L)).isPresent();     // la ligne existe toujours
        assertThat(produitService.lister(7L)).hasSize(6); // l'agence voit tout
    }

    @Test
    void consulterCatalogue_refuseUneAgenceNonValidee() {
        assertThatThrownBy(() -> produitService.consulterCatalogue(8L))
                .isInstanceOf(RegleMetierException.class);
    }
}
