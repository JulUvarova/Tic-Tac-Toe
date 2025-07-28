package com.school21.Tic_Tac_Toe.exception;

public class InvalidGameIdException extends RuntimeException {
    public InvalidGameIdException(String message) {
        super(message);
    }
}
