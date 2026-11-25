package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommandeRepository extends JpaRepository<CommandeGroupee, Long> {
    boolean existsByOrganisateurIdAndStatutIn(Long organisateurId, Collection<StatutCommande> statuts);
}
