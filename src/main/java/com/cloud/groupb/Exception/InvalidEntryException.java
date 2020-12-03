package com.cloud.groupb.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidEntryException extends RuntimeException {
    public InvalidEntryException(String reason) {
        super(String.format("Entrée invalide : %s",reason));
    }
}