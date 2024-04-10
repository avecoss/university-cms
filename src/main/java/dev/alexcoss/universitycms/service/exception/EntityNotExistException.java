package dev.alexcoss.universitycms.service.exception;

public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException(String message) {
        super(message);
    }
}
