package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import com.tp.livraisonentrevoisin.dao.entities.Quartier;
import com.tp.livraisonentrevoisin.dao.entities.Voisin;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import com.tp.livraisonentrevoisin.dao.repositories.*;
import com.tp.livraisonentrevoisin.exception.RegleMetierException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuartierService {

    // États de commande qui empêchent un utilisateur de changer de quartier
    private static final List<StatutCommande> EN_COURS =
            List.of(StatutCommande.OUVERTE, StatutCommande.CLOTUREE, StatutCommande.CONFIRMEE, StatutCommande.LIVREE);

    private final QuartierRepository quartierRepository;
    private final VoisinRepository voisinRepository;
    private final AgenceRepository agenceRepository;
    private final ParticipationRepository participationRepository;

    @Transactional(readOnly = true)
    public List<Quartier> listerQuartiers(){
        return quartierRepository.findAll();
    }

    public Voisin changerQuartier(Long voisinId, Long quartierId){
        Voisin voisin = (Voisin) voisinRepository.findById(voisinId)
                .orElseThrow(() -> new RegleMetierException("Voisin introuvable"));

        Quartier quartier = quartierExistant(quartierId);

        if (voisin.getQuartier().getId().equals(quartierId)) {
            throw new RegleMetierException("Le voisin est déjà dans ce quartier");
        }
        if (participationRepository.existsByVoisinIdAndCommandeStatutIn(voisinId, EN_COURS)) {
            throw new RegleMetierException("Impossible de changer de quartier tant que vous avez une commande groupée en cours");
        }

        voisin.setQuartier(quartier);
        return voisinRepository.save(voisin);
    }

    public AgenceLivraison desservir(Long agenceId, Long quartierId){
        AgenceLivraison agence = agenceExistante(agenceId);
        Quartier quartier = quartierExistant(quartierId);

        if (!agence.getQuartiers().add(quartier)) {
            throw new RegleMetierException("Ce quartier est déjà desservi par votre agence");
        }
        return agenceRepository.save(agence);
    }

    public AgenceLivraison cesserDeDesservir(Long agenceId, Long quartierId) {
        AgenceLivraison agence = agenceExistante(agenceId);
        Quartier quartier = quartierExistant(quartierId);
        if (!agence.getQuartiers().contains(quartier)) {
            throw new RegleMetierException("Ce quartier n'est pas desservi par votre agence");
        }
        if (agence.getQuartiers().size() == 1) {
            throw new RegleMetierException("Une agence doit desservir au moins un quartier");
        }
        agence.getQuartiers().remove(quartier);
        return agenceRepository.save(agence);
    }

    private Quartier quartierExistant(Long id) {
        return quartierRepository.findById(id)
                .orElseThrow(() -> new RegleMetierException("Quartier introuvable"));
    }

    private AgenceLivraison agenceExistante(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new RegleMetierException("Agence introuvable"));
    }

}
