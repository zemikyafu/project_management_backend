package org.project_management.presentation.shared;

import org.project_management.application.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalResponse> handleBadRequestException(BadRequestException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnableToSaveResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalResponse> handleUnableToSaveResourceException(UnableToSaveResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToUpdateResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalResponse> handleUnableToUpdateResourceException(UnableToUpdateResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToDeleteResourceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalResponse> handleUnableToDeleteResourceException(UnableToDeleteResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<GlobalResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.NOT_FOUND.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Invalid data format"));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<GlobalResponse.ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
                .toList();

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Invalid JSON"));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }
}
