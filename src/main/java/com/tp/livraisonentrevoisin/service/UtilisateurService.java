package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Quartier;
import com.tp.livraisonentrevoisin.dao.entities.Voisin;
import com.tp.livraisonentrevoisin.dao.repositories.AgenceRepository;
import com.tp.livraisonentrevoisin.dao.repositories.QuartierRepository;
import com.tp.livraisonentrevoisin.dao.repositories.UtilisateurRepository;
import com.tp.livraisonentrevoisin.dao.repositories.VoisinRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final VoisinRepository voisinRepository;
    private final AgenceRepository agenceRepository;
    private final QuartierRepository quartierRepository;
    private final PasswordEncoder passwordEncoder;

    public Voisin inscrireVoisin(String nom, String email, String motDePasse, String adresse, Long quartierId){

        verifierEmailLibre(email);
        Quartier quartier = quartierRepository.findById(quartierId).orElseThrow(() -> new RegleMetierException("Quartier non trouvé"));

        Voisin voisin = new Voisin();
        voisin.setNom(nom);
        voisin.setEmail(email);
        voisin.setMotDePasse(passwordEncoder.encode(motDePasse));
        voisin.setAdresse(adresse);
        voisin.setQuartier(quartier);
        return voisinRepository.save(voisin);
    }

    public AgenceLivraison inscrireAgence(String nom, String email, String motDePasse, String nomAgence, List<Long> quartierIds){

        verifierEmailLibre(email);

        AgenceLivraison agence = new AgenceLivraison();
        agence.setNom(nom);
        agence.setEmail(email);
        agence.setMotDePasse(passwordEncoder.encode(motDePasse));
        agence.setNomAgence(nomAgence);
        agence.setValide(false);
        agence.setQuartiers(new HashSet<>(quartierRepository.findAllById(quartierIds)));
        return agenceRepository.save(agence);
    }

    private void verifierEmailLibre(String email){
        if(utilisateurRepository.existsByEmail(email)){
            throw new RegleMetierException("Un compte existe déjà avec cet email");
        }
    }
}
