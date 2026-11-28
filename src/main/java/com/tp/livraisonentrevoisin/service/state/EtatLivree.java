package com.tp.livraisonentrevoisin.service.state;

// Commande livrée : aucune action de la partie B n'est permise.
public class EtatLivree extends EtatAbstrait {

    @Override
    protected String nom() {
        return "livré";
    }
}