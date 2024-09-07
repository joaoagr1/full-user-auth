package com.authentication.module.exceptions;

import com.authentication.module.dtos.ErrorResponseDTO;
import com.authentication.module.exceptions.custom.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError firstError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String errorMessage = (firstError != null) ? firstError.getDefaultMessage() : "Validation error";

        return new ResponseEntity<>(new ErrorResponseDTO(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ErrorResponseDTO(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO("Credenciais inválidas"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleLoginAlreadyExistsException(LoginAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleDocumentAlreadyExistsException(DocumentAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseDTO> handleUserNotVerifiedException(UserNotVerifiedException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTokenException(InvalidTokenException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

@ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
