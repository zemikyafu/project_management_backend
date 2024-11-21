package org.project_management.presentation.shared;

import org.project_management.application.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse> handleBadRequestException(BadRequestException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnableToSaveResourceException.class)
    public ResponseEntity<GlobalResponse> handleUnableToSaveResourceException(UnableToSaveResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToUpdateResourceException.class)
    public ResponseEntity<GlobalResponse> handleUnableToUpdateResourceException(UnableToUpdateResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnableToDeleteResourceException.class)
    public ResponseEntity<GlobalResponse> handleUnableToDeleteResourceException(UnableToDeleteResourceException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.NOT_FOUND.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Invalid data format"));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<GlobalResponse.ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
                .toList();

        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Invalid JSON"));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<GlobalResponse> handleUserAlreadyExistException(UserAlreadyExistException e) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.CONFLICT.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.CONFLICT.value());
    }
    @ExceptionHandler(EmailException.class)
    public ResponseEntity<GlobalResponse> handleEmailException(EmailException e) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(e.getMessage()));
        GlobalResponse globalResponse = new GlobalResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errors);
        return new ResponseEntity<>(globalResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
