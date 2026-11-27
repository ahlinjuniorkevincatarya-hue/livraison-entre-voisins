package com.tp.livraisonentrevoisin.service.scheduler;

import com.tp.livraisonentrevoisin.service.CommandeGroupeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClotureAutomatique {

    private final CommandeGroupeeService commandeGroupeeService;

    // F9 - toutes les 60 secondes, on clôture les groupes ouverts dont l'heure limite est passée
    @Scheduled(fixedRate = 60_000)
    public void cloturerGroupesExpires() {
        int nombre = commandeGroupeeService.cloturerGroupesExpires();
        if (nombre > 0) {
            log.info("{} groupe(s) clôturé(s) automatiquement", nombre);
        }
    }
}