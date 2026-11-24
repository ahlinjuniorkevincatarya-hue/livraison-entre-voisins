package com.tp.livraisonentrevoisin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.repositories.ParticipationRepository;
import com.tp.livraisonentrevoisin.service.strategy.PartageEgal;
import com.tp.livraisonentrevoisin.service.strategy.PartageProportionnel;
import com.tp.livraisonentrevoisin.service.strategy.StrategiePartage;

@Service
public class FraisService {

    private final ParticipationRepository participationRepository;
    private final PartageEgal partageEgal;
    private final PartageProportionnel partageProportionnel;

    private StrategiePartage strategie; // la méthode de calcul actuellement utilisée

    public FraisService(ParticipationRepository participationRepository,
                        PartageEgal partageEgal,
                        PartageProportionnel partageProportionnel) {
        this.participationRepository = participationRepository;
        this.partageEgal = partageEgal;
        this.partageProportionnel = partageProportionnel;
        this.strategie = partageEgal; // par défaut : partage égal
    }

    // F12 - calculer la part d'un participant
    @Transactional(readOnly = true)
    public double calculerPart(Long participationId) {
        Participation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("Participation introuvable"));
        return strategie.calculerPart(participation, participation.getCommande());
    }

    // F12 - changer la méthode de calcul
    public void changerStrategie(StrategiePartage nouvelleStrategie) {
        this.strategie = nouvelleStrategie;
    }

    // raccourcis pour choisir une des deux stratégies existantes
    public void utiliserPartageEgal() {
        this.strategie = partageEgal;
    }

    public void utiliserPartageProportionnel() {
        this.strategie = partageProportionnel;
    }
}