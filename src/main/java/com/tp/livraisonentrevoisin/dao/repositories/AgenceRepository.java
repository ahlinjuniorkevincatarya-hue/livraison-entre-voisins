package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenceRepository extends JpaRepository<AgenceLivraison, Long> {
    List<AgenceLivraison> findByValideTrueAndQuartiersId(Long quartierId);

    List<AgenceLivraison> findAllByValideFalse();
}
