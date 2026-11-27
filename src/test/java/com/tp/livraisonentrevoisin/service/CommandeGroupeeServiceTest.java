package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // chaque test est annulé à la fin : la base reste propre
class CommandeGroupeeServiceTest {

    @Autowired
    private CommandeGroupeeService service;

    // Données de data.sql :
    // Voisins : 2 Amine, 3 Sara, 4 Youssef (quartier 1) ; 5 Lina (quartier 2) ; 6 Omar (quartier 2, bloqué)
    // Agences : 7 validée (quartiers 1 et 2) ; 8 non validée (quartier 1)
    // Groupes : 1 OUVERTE (Amine, avec articles) ; 2 CLOTUREE (Youssef) ; 3 CONFIRMEE (Amine)

    private LocalDateTime demain() {
        return LocalDateTime.now().plusDays(1);
    }

    // ---------- F6 : ouvrir ----------

    @Test
    void ouvrir_creeUnGroupeOuvert() {
        CommandeGroupee groupe = service.ouvrir(3L, 7L, demain(), "Hall du 8 rue d'Alger");
        assertEquals(StatutCommande.OUVERTE, groupe.getStatut());
        assertEquals("Hall du 8 rue d'Alger", groupe.getPointRetrait());
    }

    @Test
    void ouvrir_avecUneAgenceNonValidee_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.ouvrir(3L, 8L, demain(), "Hall"));
    }

    @Test
    void ouvrir_avecUneHeurePassee_estRefuse() {
        assertThrows(RegleMetierException.class,
                () -> service.ouvrir(3L, 7L, LocalDateTime.now().minusHours(1), "Hall"));
    }

    @Test
    void ouvrir_avecUnCompteBloque_estRefuse() {
        // Omar (6) est bloqué
        assertThrows(RegleMetierException.class, () -> service.ouvrir(6L, 7L, demain(), "Hall"));
    }

    // ---------- F7 : lister ----------

    @Test
    void lister_unVoisinDeMaarif_voitLeGroupe1() {
        assertTrue(service.listerOuvertes(3L).stream()
                .anyMatch(groupe -> groupe.getId().equals(1L)));
    }

    @Test
    void lister_unVoisinDeGauthier_neVoitPasLeGroupe1() {
        assertTrue(service.listerOuvertes(5L).stream()
                .noneMatch(groupe -> groupe.getId().equals(1L)));
    }

    // ---------- F9 : clôturer ----------

    @Test
    void cloturer_unGroupeAvecArticles_devientCloture() {
        assertEquals(StatutCommande.CLOTUREE, service.cloturer(1L, 2L).getStatut());
    }

    @Test
    void cloturer_parQuelquUnQuiNestPasLOrganisateur_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.cloturer(1L, 3L));
    }

    @Test
    void cloturer_unGroupeDejaConfirme_estRefuseParLePatternState() {
        assertThrows(RegleMetierException.class, () -> service.cloturer(3L, 2L));
    }

    @Test
    void clotureAutomatique_unGroupeSansArticle_estAnnule() {
        CommandeGroupee groupe = service.ouvrir(3L, 7L, demain(), "Hall");
        groupe.setHeureLimite(LocalDateTime.now().minusMinutes(1)); // on fait comme si l'heure était passée
        assertTrue(service.cloturerGroupesExpires() >= 1);
        assertEquals(StatutCommande.ANNULEE, groupe.getStatut());
    }

    // ---------- F10 : annuler ----------

    @Test
    void annuler_unGroupeCloture_devientAnnule() {
        assertEquals(StatutCommande.ANNULEE, service.annuler(2L, 4L).getStatut());
    }

    @Test
    void annuler_unGroupeConfirme_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.annuler(3L, 2L));
    }

    // ---------- F11 : confirmer ----------

    @Test
    void confirmer_parLAgenceDuGroupe_fixeLesFrais() {
        CommandeGroupee groupe = service.confirmer(2L, 7L, 25.0);
        assertEquals(StatutCommande.CONFIRMEE, groupe.getStatut());
        assertEquals(25.0, groupe.getFraisLivraison(), 0.001);
    }

    @Test
    void confirmer_parUneAutreAgence_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.confirmer(2L, 8L, 25.0));
    }

    @Test
    void confirmer_unGroupeEncoreOuvert_estRefuse() {
        assertThrows(RegleMetierException.class, () -> service.confirmer(1L, 7L, 25.0));
    }

    @Test
    void lister_lesCommandesDeLAgence7_contientLeGroupe2() {
        assertTrue(service.listerCommandesAgence(7L).stream()
                .anyMatch(groupe -> groupe.getId().equals(2L)));
    }
}