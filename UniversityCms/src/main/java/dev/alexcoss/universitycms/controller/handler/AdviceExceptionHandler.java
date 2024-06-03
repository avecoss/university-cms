package dev.alexcoss.universitycms.controller.handler;

import dev.alexcoss.universitycms.util.exception.EntityNotExistException;
import dev.alexcoss.universitycms.util.exception.IllegalEntityException;
import dev.alexcoss.universitycms.util.exception.NullEntityListException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler(EntityNotExistException.class)
    public String handleEntityNotExistExceptions(EntityNotExistException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        log.error("Entity not found: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(IllegalEntityException.class)
    public String handleIllegalEntityExceptions(MethodArgumentNotValidException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Validation error: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "errors/400";
    }

    @ExceptionHandler(NullEntityListException.class)
    public String handleNullEntityListExceptions(NullEntityListException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Entity list is null or empty: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "errors/400";
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("An error occurred: {}", ex.getMessage());
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        return "errors/500";
    }
}
