package by.academy.jee.web.handler;

import by.academy.jee.exception.NotFoundException;
import by.academy.jee.exception.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class, DataAccessException.class, ServiceException.class})
    public ResponseEntity<String> handleBadRequestExceptions(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}