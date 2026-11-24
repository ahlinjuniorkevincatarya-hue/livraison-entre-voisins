package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
