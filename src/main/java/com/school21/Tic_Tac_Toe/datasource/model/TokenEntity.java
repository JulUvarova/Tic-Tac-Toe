package com.school21.Tic_Tac_Toe.datasource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String refreshToken;
}
