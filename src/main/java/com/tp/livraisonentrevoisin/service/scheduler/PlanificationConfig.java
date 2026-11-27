package com.tp.livraisonentrevoisin.service.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Active les tâches planifiées (@Scheduled) dans l'application.
// Sans cette annotation, Spring ignore les méthodes marquées @Scheduled.
@Configuration
@EnableScheduling
public class PlanificationConfig {
}