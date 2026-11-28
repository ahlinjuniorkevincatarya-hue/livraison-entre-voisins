package com.tp.livraisonentrevoisin.service.state;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;

public class EtatOuverte extends EtatAbstrait {

    @Override
    protected String nom() {
        return "ouvert";
    }

    // F9 - règle de gestion : à la clôture, un groupe sans aucun article est annulé.
    @Override
    public void cloturer(CommandeGroupee commande) {
        boolean aDesArticles = commande.getParticipations().stream()
                .anyMatch(participation -> !participation.getLignes().isEmpty());
        commande.setStatut(aDesArticles ? StatutCommande.CLOTUREE : StatutCommande.ANNULEE);
    }

    // F10 - un groupe ouvert peut être annulé.
    @Override
    public void annuler(CommandeGroupee commande) {
        commande.setStatut(StatutCommande.ANNULEE);
    }
}