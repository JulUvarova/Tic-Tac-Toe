package com.trumpecy.tictactoe.web.model.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @NotBlank(message = "Login is required")
    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]+$", message = "Login can contain only latin letters, numbers and special characters @$!%*?&")
    private String login;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]+$", message = "Password can contain only latin letters, numbers and special characters @$!%*?&")
    private String password;
}