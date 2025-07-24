package com.school21.Tic_Tac_Toe.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRatioDtoResponse {
    private UUID userId;
    private float winRatio;
}