package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByAgenceId(Long agenceId);

    List<Produit> findByAgenceIdAndActifTrue(Long agenceId);
}
