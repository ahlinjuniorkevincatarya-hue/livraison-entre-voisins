package com.tp.livraisonentrevoisin.service.state;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;

// Par défaut, toutes les actions sont interdites.
// Chaque état concret ne redéfinit que les actions qu'il autorise.
public abstract class EtatAbstrait implements EtatCommande {

    // le nom de l'état, utilisé dans les messages d'erreur ("ouvert", "clôturé"...)
    protected abstract String nom();

    protected RegleMetierException interdit(String action) {
        return new RegleMetierException(
                "Action « " + action + " » impossible : le groupe est " + nom());
    }

    @Override
    public void cloturer(CommandeGroupee commande) {
        throw interdit("clôturer");
    }

    @Override
    public void annuler(CommandeGroupee commande) {
        throw interdit("annuler");
    }

    @Override
    public void confirmer(CommandeGroupee commande, double frais) {
        throw interdit("confirmer");
    }
}