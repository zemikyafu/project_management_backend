package org.project_management.application.exceptions;

public class ResourceTakenException extends RuntimeException {
    public ResourceTakenException(String message) {
        super(message);
    }
}