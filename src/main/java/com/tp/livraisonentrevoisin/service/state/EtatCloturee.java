package com.tp.livraisonentrevoisin.service.state;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;

public class EtatCloturee extends EtatAbstrait {

    @Override
    protected String nom() {
        return "clôturé";
    }

    // F10 - un groupe clôturé peut encore être annulé (il n'est pas encore confirmé).
    @Override
    public void annuler(CommandeGroupee commande) {
        commande.setStatut(StatutCommande.ANNULEE);
    }

    // F11 - l'agence fixe les frais de livraison et confirme la commande.
    @Override
    public void confirmer(CommandeGroupee commande, double frais) {
        if (frais < 0) {
            throw new RegleMetierException("Les frais de livraison ne peuvent pas être négatifs");
        }
        commande.setFraisLivraison(frais);
        commande.setStatut(StatutCommande.CONFIRMEE);
    }
}

