package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.Voisin;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.AgenceRepository;
import com.tp.livraisonentrevoisin.dao.repositories.CommandeRepository;
import com.tp.livraisonentrevoisin.dao.repositories.VoisinRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import com.tp.livraisonentrevoisin.service.state.EtatCommande;
import com.tp.livraisonentrevoisin.service.state.Etats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Partie B : cycle de vie d'une commande groupée (F6, F7, F9, F10, F11)
@Service
@RequiredArgsConstructor
@Transactional
public class CommandeGroupeeService {

    private final CommandeRepository commandeRepository;
    private final VoisinRepository voisinRepository;
    private final AgenceRepository agenceRepository;

    // F6 - ouvrir un groupe
    // (c'est la Facade de Kevin qui ajoute ensuite l'organisateur comme participant)
    public CommandeGroupee ouvrir(Long organisateurId, Long agenceId,
                                  LocalDateTime heureLimite, String pointRetrait) {
        Voisin organisateur = voisinActif(organisateurId);

        AgenceLivraison agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new RegleMetierException("Agence introuvable"));
        if (!agence.isValide()) {
            throw new RegleMetierException("Cette agence n'a pas encore été validée par l'administrateur");
        }

        Long quartierId = organisateur.getQuartier().getId();
        boolean dessertLeQuartier = agence.getQuartiers().stream()
                .anyMatch(quartier -> quartier.getId().equals(quartierId));
        if (!dessertLeQuartier) {
            throw new RegleMetierException("Cette agence ne dessert pas votre quartier");
        }

        if (heureLimite == null || !heureLimite.isAfter(LocalDateTime.now())) {
            throw new RegleMetierException("L'heure limite doit être dans le futur");
        }
        if (pointRetrait == null || pointRetrait.isBlank()) {
            throw new RegleMetierException("Le point de retrait est obligatoire");
        }

        CommandeGroupee commande = new CommandeGroupee();   // statut OUVERTE par défaut
        commande.setOrganisateur(organisateur);
        commande.setAgence(agence);
        commande.setHeureLimite(heureLimite);
        commande.setPointRetrait(pointRetrait.trim());
        return commandeRepository.save(commande);
    }

    // F7 - les groupes ouverts du quartier du voisin
    @Transactional(readOnly = true)
    public List<CommandeGroupee> listerOuvertes(Long voisinId) {
        Voisin voisin = voisinRepository.findById(voisinId)
                .orElseThrow(() -> new RegleMetierException("Voisin introuvable"));
        return commandeRepository.findByOrganisateurQuartierIdAndStatutOrderByHeureLimiteAsc(
                voisin.getQuartier().getId(), StatutCommande.OUVERTE);
    }

    // détail d'un groupe
    @Transactional(readOnly = true)
    public CommandeGroupee trouver(Long groupeId) {
        return commandeRepository.findById(groupeId)
                .orElseThrow(() -> new RegleMetierException("Groupe introuvable"));
    }

    // F9 - clôture manuelle par l'organisateur
    public CommandeGroupee cloturer(Long groupeId, Long voisinId) {
        CommandeGroupee commande = trouver(groupeId);
        verifierOrganisateur(commande, voisinId);
        etat(commande).cloturer(commande);
        // pas de save() : la transaction enregistre le changement toute seule (dirty checking)
        return commande;
    }

    // F9 - clôture automatique des groupes dont l'heure limite est passée
    public int cloturerGroupesExpires() {
        List<CommandeGroupee> expires = commandeRepository.findByStatutAndHeureLimiteBefore(
                StatutCommande.OUVERTE, LocalDateTime.now());
        for (CommandeGroupee commande : expires) {
            etat(commande).cloturer(commande);
        }
        return expires.size();
    }

    // F10 - annuler (seulement l'organisateur, et avant la confirmation)
    public CommandeGroupee annuler(Long groupeId, Long voisinId) {
        CommandeGroupee commande = trouver(groupeId);
        verifierOrganisateur(commande, voisinId);
        etat(commande).annuler(commande);
        return commande;
    }

    // F11 - les commandes reçues par une agence
    @Transactional(readOnly = true)
    public List<CommandeGroupee> listerCommandesAgence(Long agenceId) {
        return commandeRepository.findByAgenceIdOrderByDateCreationDesc(agenceId);
    }

    // F11 - l'agence confirme la commande et fixe les frais de livraison
    public CommandeGroupee confirmer(Long groupeId, Long agenceId, double frais) {
        CommandeGroupee commande = trouver(groupeId);
        if (!commande.getAgence().getId().equals(agenceId)) {
            throw new RegleMetierException("Cette commande ne concerne pas votre agence");
        }
        etat(commande).confirmer(commande, frais);
        return commande;
    }

    // F13 - partie C (Yassin) : on laisse ces deux méthodes comme Kevin les a écrites
    public CommandeGroupee marquerLivree(Long groupeId, Long voisinId) {
        throw new UnsupportedOperationException("À implémenter (F13)");
    }

    public CommandeGroupee terminer(Long groupeId) {
        throw new UnsupportedOperationException("À implémenter (F13)");
    }

    // ----- méthodes privées -----

    private EtatCommande etat(CommandeGroupee commande) {
        return Etats.pour(commande.getStatut());
    }

    private Voisin voisinActif(Long voisinId) {
        Voisin voisin = voisinRepository.findById(voisinId)
                .orElseThrow(() -> new RegleMetierException("Voisin introuvable"));
        if (!voisin.isActif()) {
            throw new RegleMetierException("Votre compte est bloqué");
        }
        return voisin;
    }

    private void verifierOrganisateur(CommandeGroupee commande, Long voisinId) {
        if (!commande.getOrganisateur().getId().equals(voisinId)) {
            throw new RegleMetierException("Seul l'organisateur peut faire cette action");
        }
    }
}