package com.tp.livraisonentrevoisin.dao.repositories;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import com.tp.livraisonentrevoisin.dao.enums.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CommandeRepository extends JpaRepository<CommandeGroupee, Long> {

    // F3 (Kevin) - un organisateur a-t-il une commande dans un de ces statuts ?
    boolean existsByOrganisateurIdAndStatutIn(Long organisateurId, Collection<StatutCommande> statuts);

    // F7 - les groupes d'un statut donné, dans le quartier de l'organisateur, triés par heure limite
    List<CommandeGroupee> findByOrganisateurQuartierIdAndStatutOrderByHeureLimiteAsc(
            Long quartierId, StatutCommande statut);

    // F9 - les groupes d'un statut donné dont l'heure limite est passée
    List<CommandeGroupee> findByStatutAndHeureLimiteBefore(
            StatutCommande statut, LocalDateTime maintenant);

    // F11 - les commandes reçues par une agence, les plus récentes d'abord
    List<CommandeGroupee> findByAgenceIdOrderByDateCreationDesc(Long agenceId);
}