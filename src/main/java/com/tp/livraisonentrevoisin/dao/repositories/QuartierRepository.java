package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    Optional<Quartier> findByNomIgnoreCaseAndVilleIgnoreCase(String nom, String ville);
}
