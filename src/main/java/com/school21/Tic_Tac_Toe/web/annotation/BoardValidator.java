package com.school21.Tic_Tac_Toe.web.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BoardValidator implements ConstraintValidator<ValidBoard, int[][]> {

    @Override
    public void initialize(ValidBoard constraintAnnotation) {
    }

    @Override
    public boolean isValid(int[][] board, ConstraintValidatorContext context) {
        if (board == null) {
            return false;
        }
        if (board.length != 3) {
            return false;
        }
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null || board[i].length != 3) {
                return false;
            }
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0 && board[i][j] != 1 && board[i][j] != 2) {
                    return false;
                }
            }
        }

        return true;
    }
}