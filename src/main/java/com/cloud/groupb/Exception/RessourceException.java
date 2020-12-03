package com.cloud.groupb.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RessourceException extends RuntimeException {

    public RessourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Impossible d'obtenir la ressource %s qui a pour %s la valeur '%s'", resourceName, fieldName, fieldValue));

    }
}