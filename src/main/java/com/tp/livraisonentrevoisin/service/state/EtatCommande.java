package com.tp.livraisonentrevoisin.service.state;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;

// Pattern State : chaque statut d'une commande groupée est représenté par une classe
// qui décide si une action est permise et vers quel statut la commande passe.
public interface EtatCommande {

    void cloturer(CommandeGroupee commande);                  // F9

    void annuler(CommandeGroupee commande);                   // F10

    void confirmer(CommandeGroupee commande, double frais);   // F11
}