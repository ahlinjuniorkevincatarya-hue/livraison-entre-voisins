package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.tp.livraisonentrevoisin.dao.entities.Voisin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class QuartierServiceTest {

    @Autowired
    QuartierService quartierService;
    @Autowired UtilisateurService utilisateurService;

    @Test
    void listerQuartiers_retourneLesQuartiersDuJeuDeTest() {
        assertThat(quartierService.listerQuartiers()).hasSize(2);
    }

    @Test
    void changerQuartier_ok_sansCommandeEnCours() {
        Voisin v = utilisateurService.inscrireVoisin("Nadia", "nadia@mail.ma", "secret123", "adresse", 1L);
        Voisin modifie = quartierService.changerQuartier(v.getId(), 2L);
        assertThat(modifie.getQuartier().getId()).isEqualTo(2L);
    }

    @Test
    void changerQuartier_refuseSiCommandeEnCours() {
        assertThatThrownBy(() -> quartierService.changerQuartier(2L, 2L))   // Amine participe au groupe G1
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("en cours");
    }

    @Test
    void changerQuartier_refuseLeMemeQuartier() {
        Voisin v = utilisateurService.inscrireVoisin("Nadia", "nadia@mail.ma", "secret123", "adresse", 1L);
        assertThatThrownBy(() -> quartierService.changerQuartier(v.getId(), 1L))
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("déjà");
    }

    @Test
    void desservir_ajouteUnQuartier_puisRefuseLeDoublon() {
        AgenceLivraison a = quartierService.desservir(8L, 2L);
        assertThat(a.getQuartiers()).hasSize(2);

        assertThatThrownBy(() -> quartierService.desservir(7L, 1L))         // 7 dessert déjà le quartier 1
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("déjà desservi");
    }

    @Test
    void cesserDeDesservir_refuseLeDernierQuartier() {
        assertThat(quartierService.cesserDeDesservir(7L, 2L).getQuartiers()).hasSize(1);

        assertThatThrownBy(() -> quartierService.cesserDeDesservir(8L, 1L)) // 8 ne dessert que le quartier 1
                .isInstanceOf(RegleMetierException.class).hasMessageContaining("au moins un quartier");
    }
}
