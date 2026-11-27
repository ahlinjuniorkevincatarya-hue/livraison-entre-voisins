package com.tp.livraisonentrevoisin.service;

import com.tp.livraisonentrevoisin.dao.entities.CommandeGroupee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommandeGroupeeService {

    public CommandeGroupee ouvrir(Long organisateurId, Long agenceId,
                                  LocalDateTime heureLimite, String pointRetrait) {
        throw new UnsupportedOperationException("À implémenter (F6)");
    }

    @Transactional(readOnly = true)
    public List<CommandeGroupee> listerOuvertes(Long voisinId) {
        throw new UnsupportedOperationException("À implémenter (F7)");
    }

    public CommandeGroupee cloturer(Long groupeId, Long voisinId) {
        throw new UnsupportedOperationException("À implémenter (F9)");
    }

    public CommandeGroupee annuler(Long groupeId, Long voisinId) {
        throw new UnsupportedOperationException("À implémenter (F10)");
    }

    public CommandeGroupee confirmer(Long groupeId, Long agenceId, double frais) {
        throw new UnsupportedOperationException("À implémenter (F11)");
    }

    public CommandeGroupee marquerLivree(Long groupeId, Long voisinId) {
        throw new UnsupportedOperationException("À implémenter (F13)");
    }

    public CommandeGroupee terminer(Long groupeId) {
        throw new UnsupportedOperationException("À implémenter (F13)");
    }
}
