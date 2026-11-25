package com.tp.livraisonentrevoisin.service.strategy;

import org.springframework.stereotype.Component;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.LigneArticle;
import com.tp.livraisonentrevoisin.dao.entities.Participation;

@Component
public class PartageProportionnel implements StrategiePartage {

    @Override
    public double calculerPart(Participation participation, CommandeGroupee commande) {
        // 1. total des articles de ce participant
        double monTotal = totalArticles(participation);

        // 2. total des articles de tout le groupe
        double totalGroupe = 0;
        for (Participation p : commande.getParticipations()) {
            totalGroupe += totalArticles(p);
        }

        // 3. frais de livraison (null tant que l'agence n'a pas confirmé)
        double frais = commande.getFraisLivraison() == null ? 0 : commande.getFraisLivraison();

        // 4. si le groupe n'a aucun article, on évite la division par zéro
        if (totalGroupe == 0) {
            return monTotal;
        }

        // 5. part des frais proportionnelle à ma part des articles
        return monTotal + frais * (monTotal / totalGroupe);
    }

    private double totalArticles(Participation participation) {
        double total = 0;
        for (LigneArticle ligne : participation.getLignes()) {
            total += ligne.getPrixUnitaire() * ligne.getQuantite();
        }
        return total;
    }
}