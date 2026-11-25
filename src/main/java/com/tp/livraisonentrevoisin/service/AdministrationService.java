package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Quartier;
import com.tp.livraisonentrevoisin.dao.entities.Utilisateur;
import com.tp.livraisonentrevoisin.dao.enums.Role;
import com.tp.livraisonentrevoisin.dao.repositories.AgenceRepository;
import com.tp.livraisonentrevoisin.dao.repositories.QuartierRepository;
import com.tp.livraisonentrevoisin.dao.repositories.UtilisateurRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdministrationService {

    private final UtilisateurService utilisateurService;
    private final AgenceRepository agenceRepository;
    private final QuartierRepository quartierRepository;
    private final UtilisateurRepository utilisateurRepository;

    //Agences
    @Transactional(readOnly = true)
    public List<AgenceLivraison> listerAgencesAValider(Long adminId){
        verifierAdmin(adminId);
        return agenceRepository.findAllByValideFalse();
    }

    public AgenceLivraison validerAgence(Long adminId, Long agenceId){
        verifierAdmin(adminId);
        AgenceLivraison agence = agenceRepository.findById(agenceId).orElseThrow(()-> new RegleMetierException("Agence introuvable"));

        if(agence.isValide()){
            throw new RegleMetierException("Agence déjà validée");
        }
        agence.setValide(true);
        return agenceRepository.save(agence);
    }

    // Comptes

    public Utilisateur bloquer(Long adminId, Long userId){
        verifierAdmin(adminId);
        if(adminId.equals(userId)){
            throw new RegleMetierException("Vous ne pouvez pas bloquer votre propre compte");
        }
        Utilisateur user = utilisateurExistant(userId);
        if(user.getRole() == Role.ADMIN){
            throw new RegleMetierException("Vous ne pouvez pas bloquer un autre administrateur");
        }
        if(!user.isActif()){
            throw new RegleMetierException("Utilisateur déjà bloqué");
        }
        user.setActif(false);
        return utilisateurRepository.save(user);
    }

    public Utilisateur debloquer(Long adminId, Long userId){
        verifierAdmin(adminId);
        Utilisateur user = utilisateurExistant(userId);
        if(user.isActif()){
            throw new RegleMetierException("Ce compte n'est pas bloqué");
        }
        user.setActif(true);
        return utilisateurRepository.save(user);
    }

    // Quartiers
    public Quartier creerQuartier(Long adminId, String nom, String ville){
        verifierAdmin(adminId);
        verifierNomEtVille(nom, ville);
        if (quartierRepository.findByNomIgnoreCaseAndVilleIgnoreCase(nom.trim(), ville.trim()).isPresent()){
            throw new RegleMetierException("Un quartier avec ce nom et cette ville existe déjà");
        }
        Quartier quartier = new Quartier();
        quartier.setNom(nom.trim());
        quartier.setVille(ville.trim());
        return quartierRepository.save(quartier);
    }

    public Quartier modifierQuartier(Long adminId, Long quartierId, String nom, String ville){
        verifierAdmin(adminId);
        verifierNomEtVille(nom, ville);
        Quartier quartier = quartierRepository.findById(quartierId).orElseThrow(()-> new RegleMetierException("Quartier introuvable"));

        quartierRepository.findByNomIgnoreCaseAndVilleIgnoreCase(nom.trim(), ville.trim())
                .filter(autre -> !autre.getId().equals(quartierId))
                .ifPresent(autre -> { throw new RegleMetierException("Ce quartier existe déjà"); });

        quartier.setNom(nom.trim());
        quartier.setVille(ville.trim());
        return quartierRepository.save(quartier);
    }

    //Méthodes privées
    private void verifierAdmin(Long adminId){
        Utilisateur admin = utilisateurExistant(adminId);
        if(admin.getRole() != Role.ADMIN){
            throw new RegleMetierException("Action réservée aux administrateurs");
        }
    }

    private Utilisateur utilisateurExistant(Long userId){
        return utilisateurRepository.findById(userId).orElseThrow(()-> new RegleMetierException("Utilisateur introuvable"));
    }

    private void verifierNomEtVille(String nom, String ville){
        if(nom == null || nom.trim().isEmpty()){
            throw new RegleMetierException("Le nom du quartier ne peut pas être vide");
        }
        if(ville == null || ville.trim().isEmpty()){
            throw new RegleMetierException("La ville du quartier ne peut pas être vide");
        }
    }
}
