package com.school21.Tic_Tac_Toe.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDtoResponse {
    private UUID userId;
    private int wins;
    private int losses;
    private int draws;
    private float winRatio;
}
