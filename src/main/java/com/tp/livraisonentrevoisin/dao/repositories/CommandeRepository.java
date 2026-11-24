package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<CommandeGroupee, Long> {
}
