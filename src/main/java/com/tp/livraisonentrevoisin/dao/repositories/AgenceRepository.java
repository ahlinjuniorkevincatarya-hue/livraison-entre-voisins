package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.AgenceLivraison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenceRepository extends JpaRepository<AgenceLivraison, Long> {
}
