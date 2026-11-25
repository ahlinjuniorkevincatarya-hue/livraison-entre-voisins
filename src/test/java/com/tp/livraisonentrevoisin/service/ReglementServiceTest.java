package com.tp.livraisonentrevoisin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.CommandeRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;

@SpringBootTest
@Transactional
class ReglementServiceTest {

    @Autowired
    private ReglementService service;

    @Autowired
    private CommandeRepository commandeRepository;

        @Autowired
    private FraisService fraisService;

    @BeforeEach
    void remettreStrategieParDefaut() {
        fraisService.utiliserPartageEgal();
    }

    // Groupe 1 : OUVERTE, organisateur Amine (id 2)
    // Groupe 2 : CLOTUREE, organisateur Youssef (id 4)
    // Groupe 3 : CONFIRMEE, organisateur Amine (id 2), participations 5, 6, 7, frais 30
    // Groupe 4 : LIVREE, organisatrice Lina (id 5), participations 8 (deja reglee) et 9 (Omar)

    @Test
    void marquerLivree_groupeConfirme_fonctionne() {
        CommandeGroupee c = service.marquerLivree(3L, 2L);
        assertEquals(StatutCommande.LIVREE, c.getStatut());
    }

    @Test
    void marquerLivree_parQuelquunQuiNestPasOrganisateur_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.marquerLivree(3L, 3L));
    }

    @Test
    void marquerLivree_groupeNonConfirme_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.marquerLivree(1L, 2L));
    }

    @Test
    void marquerPartReglee_enregistreLePaiement() {
        Participation p = service.marquerPartReglee(9L, 5L);
        assertTrue(p.isEstRegle());
        assertNotNull(p.getPaiement());
        assertEquals(32.0, p.getPaiement().getMontant(), 0.01);
        assertNotNull(p.getPaiement().getDatePaiement());
    }

    @Test
    void marquerPartReglee_dernierePart_terminelaCommande() {
        // dans le groupe 4, seule la participation 9 reste a regler
        service.marquerPartReglee(9L, 5L);
        CommandeGroupee c = commandeRepository.findById(4L).orElseThrow();
        assertEquals(StatutCommande.TERMINEE, c.getStatut());
    }

    @Test
    void marquerPartReglee_partDejaReglee_estRefusee() {
        assertThrows(RegleMetierException.class, () -> service.marquerPartReglee(8L, 5L));
    }

    @Test
    void marquerPartReglee_commandeNonLivree_estRefusee() {
        // participation 5 est dans le groupe 3 (CONFIRMEE, pas encore livre)
        assertThrows(RegleMetierException.class, () -> service.marquerPartReglee(5L, 2L));
    }

    @Test
    void marquerPartReglee_parQuelquunQuiNestPasOrganisateur_estRefusee() {
        assertThrows(RegleMetierException.class, () -> service.marquerPartReglee(9L, 2L));
    }

    @Test
    void commandeInexistante_estRefusee() {
        assertThrows(RegleMetierException.class, () -> service.marquerLivree(999L, 2L));
    }
}