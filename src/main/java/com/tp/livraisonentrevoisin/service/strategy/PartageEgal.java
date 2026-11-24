package com.tp.livraisonentrevoisin.service.strategy;

import org.springframework.stereotype.Component;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.entities.Participation;

@Component
public class PartageEgal implements StrategiePartage {

    @Override
    public double calculerPart(Participation participation, CommandeGroupee commande) {
        // 1. total des articles de ce participant
        double totalArticles = 0;
        for (LigneArticle ligne : participation.getLignes()) {
            totalArticles += ligne.getPrixUnitaire() * ligne.getQuantite();
        }

        // 2. frais de livraison (null tant que l'agence n'a pas confirmé)
        double frais = commande.getFraisLivraison() == null ? 0 : commande.getFraisLivraison();

        // 3. même part de frais pour tout le monde
        int nombreParticipants = commande.getParticipations().size();
        return totalArticles + frais / nombreParticipants;
    }
}