package com.tp.livraisonentrevoisin.service.state;

// Commande annulée : aucune action de la partie B n'est permise.
public class EtatAnnulee extends EtatAbstrait {

    @Override
    protected String nom() {
        return "annulé";
    }
}