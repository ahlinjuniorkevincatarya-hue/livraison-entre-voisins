package com.tp.livraisonentrevoisin.service.state;

// Commande terminée : aucune action de la partie B n'est permise.
public class EtatTerminee extends EtatAbstrait {

    @Override
    protected String nom() {
        return "terminé";
    }
}