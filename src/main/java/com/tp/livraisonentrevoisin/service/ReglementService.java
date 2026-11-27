package com.tp.livraisonentrevoisin.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.Paiement;
import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.CommandeRepository;
import com.tp.livraisonentrevoisin.dao.repositories.ParticipationRepository;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReglementService {

    private final CommandeRepository commandeRepository;
    private final ParticipationRepository participationRepository;
    private final FraisService fraisService;

    // F13 : marquer la commande comme livrée
    @Transactional
    public CommandeGroupee marquerLivree(Long commandeId, Long organisateurId) {
        CommandeGroupee commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RegleMetierException("Commande introuvable"));

        verifierOrganisateur(commande, organisateurId);

        if (commande.getStatut() != StatutCommande.CONFIRMEE) {
            throw new RegleMetierException("La commande doit être confirmée avant d'être livrée");
        }

        commande.setStatut(StatutCommande.LIVREE);
        return commandeRepository.save(commande);
    }

    // F13 : marquer la part d'un participant comme réglée
    @Transactional
    public Participation marquerPartReglee(Long participationId, Long organisateurId) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new RegleMetierException("Participation introuvable"));
        CommandeGroupee commande = participation.getCommande();

        verifierOrganisateur(commande, organisateurId);

        if (commande.getStatut() != StatutCommande.LIVREE) {
            throw new RegleMetierException("La commande doit être livrée avant de régler les parts");
        }
        if (participation.isEstRegle()) {
            throw new RegleMetierException("Cette part est déjà réglée");
        }

        // montant calculé par F12
        Paiement paiement = new Paiement();
        paiement.setMontant(fraisService.calculerPart(participationId));
        paiement.setDatePaiement(LocalDate.now());

        participation.setPaiement(paiement);
        participation.setEstRegle(true);
        participationRepository.save(participation);

        // si toutes les parts sont réglées, la commande est terminée
        boolean toutesReglees = commande.getParticipations().stream()
                .allMatch(Participation::isEstRegle);
        if (toutesReglees) {
            commande.setStatut(StatutCommande.TERMINEE);
            commandeRepository.save(commande);
        }

        return participation;
    }

    // règle commune : seul l'organisateur peut agir
    private void verifierOrganisateur(CommandeGroupee commande, Long organisateurId) {
        if (!commande.getOrganisateur().getId().equals(organisateurId)) {
            throw new RegleMetierException("Seul l'organisateur peut effectuer cette action");
        }
    }
}