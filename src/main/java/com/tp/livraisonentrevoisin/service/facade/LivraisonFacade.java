package com.tp.livraisonentrevoisin.service.facade;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.service.CommandeGroupeeService;
import com.tp.livraisonentrevoisin.service.FraisService;
import com.tp.livraisonentrevoisin.service.ParticipationService;
import com.tp.livraisonentrevoisin.service.ReglementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LivraisonFacade {

    private final CommandeGroupeeService commandeGroupeeService;
    private final ParticipationService participationService;
    private final FraisService fraisService;
    private final ReglementService reglementService;

    // ---------- cycle de vie du groupe (B) ----------
    public CommandeGroupee ouvrirGroupe(Long organisateurId, Long agenceId,
                                        LocalDateTime heureLimite, String pointRetrait) {
        CommandeGroupee groupe = commandeGroupeeService.ouvrir(organisateurId, agenceId, heureLimite, pointRetrait);
        participationService.rejoindre(groupe.getId(), organisateurId);   // l'organisateur participe à son groupe
        return groupe;
    }

    @Transactional(readOnly = true)
    public List<CommandeGroupee> listerGroupesOuverts(Long voisinId) {
        return commandeGroupeeService.listerOuvertes(voisinId);
    }

    public CommandeGroupee cloturer(Long groupeId, Long voisinId) {
        return commandeGroupeeService.cloturer(groupeId, voisinId);
    }

    public CommandeGroupee annuler(Long groupeId, Long voisinId) {
        return commandeGroupeeService.annuler(groupeId, voisinId);
    }

    public CommandeGroupee confirmer(Long groupeId, Long agenceId, double frais) {
        return commandeGroupeeService.confirmer(groupeId, agenceId, frais);
    }

    // ---------- participation, articles et parts (C) ----------
    public Participation rejoindre(Long groupeId, Long voisinId) {
        return participationService.rejoindre(groupeId, voisinId);
    }

    public Participation definirLieuLivraison(Long participationId, Long voisinId, String lieu) {
        return participationService.definirLieuLivraison(participationId, voisinId, lieu);
    }

    public LigneArticle ajouterArticle(Long participationId, Long produitId, int quantite) {
        return participationService.ajouterArticle(participationId, produitId, quantite);
    }

    public LigneArticle modifierArticle(Long ligneId, int nouvelleQuantite) {
        return participationService.modifierArticle(ligneId, nouvelleQuantite);
    }

    public void retirerArticle(Long ligneId) {
        participationService.retirerArticle(ligneId);
    }

    @Transactional(readOnly = true)
    public double consulterMaPart(Long groupeId, Long voisinId) {
        Participation p = participationService.consulterParticipation(groupeId, voisinId);
        return fraisService.calculerPart(p.getId());
    }

    @Transactional(readOnly = true)
    public Map<Long, Double> calculerParts(Long groupeId) {
        return fraisService.calculerParts(groupeId);
    }

    // ---------- livraison et règlement (C, F13) ----------
    public CommandeGroupee livrer(Long groupeId, Long organisateurId) {
        return reglementService.marquerLivree(groupeId, organisateurId);
    }

    public Participation regler(Long participationId, Long organisateurId) {
        return reglementService.marquerPartReglee(participationId, organisateurId);
    }
}