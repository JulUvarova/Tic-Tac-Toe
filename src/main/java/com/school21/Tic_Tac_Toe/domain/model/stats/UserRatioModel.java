package com.school21.Tic_Tac_Toe.domain.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRatioModel {
    private UUID userId;
    private double winRatio;
}
