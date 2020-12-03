package com.cloud.groupb.entity;

import lombok.Data;

/**
 * Objet de transport utilisateur
 */
@Data
public class User {
    /**
     * Identifiant technique
     */
    private String id;
    /**
     * Prénom
     */
    private String firstName;
    /**
     * Nom
     */
    private String lastName;
    /**
     * Date de naissance
     */
    private String birthDay;
    /**
     * Localisation par coordonnées lat/lon
     */
    private Position position;
}
