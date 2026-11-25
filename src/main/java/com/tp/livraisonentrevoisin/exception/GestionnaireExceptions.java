package com.tp.livraisonentrevoisin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

// Cette classe permet de gérer les exceptions et de renvoyer des réponses appropriées aux clients de l'API. Elle est annotée avec @RestControllerAdvice, ce qui signifie qu'elle intercepte les exceptions levées par les contrôleurs REST et fournit des réponses personnalisées.

@RestControllerAdvice
public class GestionnaireExceptions {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> regleMetier(RegleMetierException e) {
        return Map.of("erreur", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validation(MethodArgumentNotValidException e) {
        Map<String, String> erreurs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> erreurs.put(f.getField(), f.getDefaultMessage()));
        return erreurs;
    }

}
