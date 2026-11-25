package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.Participation;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByVoisinIdAndCommandeStatutIn(Long voisinId, Collection<StatutCommande> statuts);
}
