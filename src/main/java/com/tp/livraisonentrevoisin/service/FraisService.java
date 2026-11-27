package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.repositories.ParticipationRepository;
import com.tp.livraisonentrevoisin.service.strategy.PartageEgal;
import com.tp.livraisonentrevoisin.service.strategy.PartageProportionnel;
import com.tp.livraisonentrevoisin.service.strategy.StrategiePartage;

import java.util.LinkedHashMap;
import java.util.Map;


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
                .orElseThrow(() -> new RegleMetierException("Participation introuvable"));
        return strategie.calculerPart(participation, participation.getCommande());
    }

    // F12 - changer la méthode de calcul
    public void changerStrategie(StrategiePartage nouvelleStrategie) {
        this.strategie = nouvelleStrategie;
    }

    // F12 - parts de tous les participants d'un groupe (clé = id de la participation)
    @Transactional(readOnly = true)
    public Map<Long, Double> calculerParts(Long groupeId) {
        Map<Long, Double> parts = new LinkedHashMap<>();
        for (Participation p : participationRepository.findByCommandeId(groupeId)) {
            parts.put(p.getId(), strategie.calculerPart(p, p.getCommande()));
        }
        return parts;
    }

    // raccourcis pour choisir une des deux stratégies existantes
    public void utiliserPartageEgal() {
        this.strategie = partageEgal;
    }

    public void utiliserPartageProportionnel() {
        this.strategie = partageProportionnel;
    }
}