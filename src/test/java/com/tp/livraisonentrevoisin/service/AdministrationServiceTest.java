package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Quartier;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class AdministrationServiceTest {

    private static final Long ADMIN = 1L;

    @Autowired
    AdministrationService admin;

    @Test
    void validerAgence_passeLAgenceAValide() {
        AgenceLivraison a = admin.validerAgence(ADMIN, 8L);
        assertThat(a.isValide()).isTrue();
        assertThat(admin.listerAgencesAValider(ADMIN)).isEmpty();
    }

    @Test
    void validerAgence_refuseUneAgenceDejaValidee() {
        assertThatThrownBy(() -> admin.validerAgence(ADMIN, 7L))
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("déjà validée");
    }

    @Test
    void validerAgence_estReserveeALAdmin() {
        assertThatThrownBy(() -> admin.validerAgence(2L, 8L))      // 2 = Amine, un voisin
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("réservée");
    }

    @Test
    void bloquer_desactiveLeCompte() {
        assertThat(admin.bloquer(ADMIN, 2L).isActif()).isFalse();
    }

    @Test
    void bloquer_refuseSonPropreCompteEtUnAutreAdmin() {
        assertThatThrownBy(() -> admin.bloquer(ADMIN, ADMIN))
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("propre compte");
    }

    @Test
    void bloquer_refuseUnCompteDejaBloque() {
        assertThatThrownBy(() -> admin.bloquer(ADMIN, 6L))          // Omar est déjà bloqué
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("déjà bloqué");
    }

    @Test
    void debloquer_reactiveLeCompte() {
        assertThat(admin.debloquer(ADMIN, 6L).isActif()).isTrue();
    }

    @Test
    void creerQuartier_ok_puisRefuseLeDoublonSansTenirCompteDeLaCasse() {
        Quartier q = admin.creerQuartier(ADMIN, "Oasis", "Casablanca");
        assertThat(q.getId()).isNotNull();

        assertThatThrownBy(() -> admin.creerQuartier(ADMIN, "  oasis ", "CASABLANCA"))
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("existe déjà");
    }

    @Test
    void creerQuartier_refuseUnNomVide() {
        assertThatThrownBy(() -> admin.creerQuartier(ADMIN, " ", "Casablanca"))
                .isInstanceOf(RegleMetierException.class);
    }

    @Test
    void modifierQuartier_refuseLeNomDUnAutreQuartier() {
        assertThatThrownBy(() -> admin.modifierQuartier(ADMIN, 2L, "Maarif", "Casablanca"))
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("existe déjà");
    }
}
