package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.repositories.ArticleRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // chaque test est annulé à la fin : la base reste propre
class ParticipationServiceTest {

    @Autowired
    private ParticipationService service;

    @Autowired
    private ArticleRepository articleRepository;

    // Participation 1 = Amine dans le groupe 1 (OUVERTE, agence 7)
    // Participation 3 = Youssef dans le groupe 2 (CLOTUREE)
    // Produits : 1 Lait (5.0), 2 Pain (2.0), 6 désactivé, 7 autre agence

    @Test
    void ajouterNouveauProduit_creeUneLigneAvecLePrixDuProduit() {
        LigneArticle ligne = service.ajouterArticle(1L, 2L, 3);
        assertEquals(3, ligne.getQuantite());
        assertEquals(2.0, ligne.getPrixUnitaire());
    }

    @Test
    void ajouterProduitDejaPresent_augmenteLaQuantite() {
        // la participation 1 a déjà 2 laits
        LigneArticle ligne = service.ajouterArticle(1L, 1L, 3);
        assertEquals(5, ligne.getQuantite());
    }

    @Test
    void ajouterDansGroupeCloture_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.ajouterArticle(3L, 1L, 1));
    }

    @Test
    void ajouterProduitDesactive_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.ajouterArticle(1L, 6L, 1));
    }

    @Test
    void ajouterProduitAutreAgence_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.ajouterArticle(1L, 7L, 1));
    }

    @Test
    void quantiteZero_estRefusee() {
        assertThrows(RegleMetierException.class, () -> service.ajouterArticle(1L, 2L, 0));
    }

    @Test
    void modifierQuantite_fonctionne() {
        LigneArticle ligne = service.modifierArticle(1L, 10);
        assertEquals(10, ligne.getQuantite());
    }

    @Test
    void modifierDansGroupeCloture_estRefuse() {
        // la ligne 4 appartient à la participation 3 (groupe clôturé)
        assertThrows(RegleMetierException.class, () -> service.modifierArticle(4L, 2));
    }

    @Test
    void retirerArticle_lSupprime() {
        service.retirerArticle(1L);
        assertTrue(articleRepository.findById(1L).isEmpty());
    }

    @Test
    void retirerDansGroupeCloture_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.retirerArticle(4L));
    }
}