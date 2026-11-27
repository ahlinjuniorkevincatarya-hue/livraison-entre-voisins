package com.tp.livraisonentrevoisin.service.state;

// Commande confirmée : on ne peut plus clôturer, annuler ni confirmer (règle F10).
public class EtatConfirmee extends EtatAbstrait {

    @Override
    protected String nom() {
        return "confirmé";
    }
}