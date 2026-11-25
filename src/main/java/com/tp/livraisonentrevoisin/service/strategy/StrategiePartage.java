package com.tp.livraisonentrevoisin.service.strategy;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.entities.Participation;

public interface StrategiePartage {
    double calculerPart(Participation participation, CommandeGroupee commande);
}