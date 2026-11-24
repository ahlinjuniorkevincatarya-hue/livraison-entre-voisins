package com.tp.livraisonentrevoisin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FraisServiceTest {

    @Autowired
    private FraisService fraisService;

    // Groupe 3 (frais 30) : participations 5 (Amine 31), 6 (Sara 22), 7 (Youssef 39)
    // Groupe 4 (frais 20) : participations 8 (Lina 57), 9 (Omar 22)
    // Groupe 1 (frais null) : participation 1 (Amine 25), 2 (Sara 8)

    @BeforeEach
    void remettreStrategieParDefaut() {
        // FraisService garde sa stratégie en mémoire : on la remet à zéro avant chaque test
        fraisService.utiliserPartageEgal();
    }

    @Test
    void partageEgal_groupe3() {
        assertEquals(41.0, fraisService.calculerPart(5L), 0.01);
        assertEquals(32.0, fraisService.calculerPart(6L), 0.01);
        assertEquals(49.0, fraisService.calculerPart(7L), 0.01);
    }

    @Test
    void partageEgal_groupe4() {
        assertEquals(67.0, fraisService.calculerPart(8L), 0.01);
        assertEquals(32.0, fraisService.calculerPart(9L), 0.01);
    }

    @Test
    void partageEgal_sansFrais_donneLeTotalDesArticles() {
        // frais = null -> traité comme 0
        assertEquals(25.0, fraisService.calculerPart(1L), 0.01);
    }

    @Test
    void partageProportionnel_groupe3() {
        fraisService.utiliserPartageProportionnel();
        assertEquals(41.11, fraisService.calculerPart(5L), 0.01);
        assertEquals(29.17, fraisService.calculerPart(6L), 0.01);
        assertEquals(51.72, fraisService.calculerPart(7L), 0.01);
    }

    @Test
    void partageProportionnel_laSommeDesPartsEgaleArticlesPlusFrais() {
        fraisService.utiliserPartageProportionnel();
        double somme = fraisService.calculerPart(5L)
                + fraisService.calculerPart(6L)
                + fraisService.calculerPart(7L);
        assertEquals(122.0, somme, 0.01); // 92 d'articles + 30 de frais
    }

    @Test
    void participationInexistante_estRefusee() {
        assertThrows(IllegalArgumentException.class, () -> fraisService.calculerPart(999L));
    }
}