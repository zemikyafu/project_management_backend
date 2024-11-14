package org.project_management.application.exceptions;

public class UnableToDeleteResourceException    extends RuntimeException{
    public UnableToDeleteResourceException(String message) {
        super(message);
    }
}
