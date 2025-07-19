package com.school21.Tic_Tac_Toe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(Exception ex) {
        String message = ex.getMessage();
        log.warn("Get status 404: {}", ex.getMessage());
        return new ApiError(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidGameIdException.class,
            InvalidMoveException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidRequestException(Exception ex) {
        String message = ex.getMessage();
        log.warn("Get status 400: {}", ex.getMessage());
        return new ApiError(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidatorException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        log.warn("Validation error: {}", message);
        return new ApiError(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknownException(Exception ex) {
        String message = ex.getMessage();
        log.warn("Get unexpected error: {}, {}", ex.getMessage(), ex.getStackTrace());
        return new ApiError(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleInvalidUserDataException(Exception ex) {
        String message = ex.getMessage();
        log.warn("Get status 401: {}", ex.getMessage());
        return new ApiError(message, HttpStatus.UNAUTHORIZED);
    }
}
