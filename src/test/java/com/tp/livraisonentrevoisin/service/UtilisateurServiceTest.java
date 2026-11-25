package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Voisin;
import com.tp.livraisonentrevoisin.dao.enums.Role;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional          // chaque test est annulé à la fin : la base reste propre
class UtilisateurServiceTest {

    @Autowired UtilisateurService utilisateurService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void inscrireVoisin_creeUnCompteAvecMotDePasseHache() {
        Voisin v = utilisateurService.inscrireVoisin(
                "Nadia", "nadia@mail.ma", "secret123", "5 rue Test", 1L);

        assertThat(v.getId()).isNotNull();
        assertThat(v.getRole()).isEqualTo(Role.VOISIN);
        assertThat(v.isActif()).isTrue();
        assertThat(v.getQuartier().getId()).isEqualTo(1L);
        assertThat(v.getMotDePasse()).isNotEqualTo("secret123");              // jamais en clair
        assertThat(passwordEncoder.matches("secret123", v.getMotDePasse())).isTrue();
    }

    @Test
    void inscrireVoisin_refuseUnEmailDejaUtilise() {
        assertThatThrownBy(() -> utilisateurService.inscrireVoisin(
                "Autre", "amine@mail.ma", "secret123", "adresse", 1L))     // amine existe dans data.sql
                .isInstanceOf(RegleMetierException.class)
                .hasMessageContaining("existe déjà");
    }

    @Test
    void inscrireVoisin_refuseUnQuartierInconnu() {
        assertThatThrownBy(() -> utilisateurService.inscrireVoisin(
                "Nadia", "nadia@mail.ma", "secret123", "adresse", 999L))
                .isInstanceOf(RegleMetierException.class)
                .hasMessageContaining("Quartier");
    }

    @Test
    void inscrireAgence_estEnAttenteDeValidation() {
        AgenceLivraison a = utilisateurService.inscrireAgence(
                "Karim", "karim@agence.ma", "secret123", "Karim Express", List.of(1L, 2L));

        assertThat(a.getId()).isNotNull();
        assertThat(a.getRole()).isEqualTo(Role.AGENCE);
        assertThat(a.isValide()).isFalse();                                // l'admin doit la valider (F14)
        assertThat(a.getQuartiers()).hasSize(2);
    }
}