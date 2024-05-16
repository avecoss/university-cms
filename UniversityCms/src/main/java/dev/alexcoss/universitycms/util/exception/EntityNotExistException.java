package dev.alexcoss.universitycms.util.exception;

public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException(String message) {
        super(message);
    }
}
