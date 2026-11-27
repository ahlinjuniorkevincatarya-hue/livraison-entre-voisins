package com.tp.livraisonentrevoisin.service.state;

import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;

// Fait le lien entre le statut enregistré en base (un enum) et l'objet état correspondant.
public final class Etats {

    // Les états n'ont aucune donnée propre : un seul exemplaire de chacun suffit.
    private static final EtatCommande ETAT_OUVERTE = new EtatOuverte();
    private static final EtatCommande ETAT_CLOTUREE = new EtatCloturee();
    private static final EtatCommande ETAT_CONFIRMEE = new EtatConfirmee();
    private static final EtatCommande ETAT_LIVREE = new EtatLivree();
    private static final EtatCommande ETAT_TERMINEE = new EtatTerminee();
    private static final EtatCommande ETAT_ANNULEE = new EtatAnnulee();

    private Etats() {
        // pas d'objet Etats : on utilise seulement Etats.pour(...)
    }

    public static EtatCommande pour(StatutCommande statut) {
        return switch (statut) {
            case OUVERTE -> ETAT_OUVERTE;
            case CLOTUREE -> ETAT_CLOTUREE;
            case CONFIRMEE -> ETAT_CONFIRMEE;
            case LIVREE -> ETAT_LIVREE;
            case TERMINEE -> ETAT_TERMINEE;
            case ANNULEE -> ETAT_ANNULEE;
        };
    }
}