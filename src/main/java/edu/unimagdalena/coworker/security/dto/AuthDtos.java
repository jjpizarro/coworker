package edu.unimagdalena.coworker.security.dto;
import edu.unimagdalena.coworker.security.domine.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class AuthDtos {
    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank String password,
            Set<Role> roles
    ) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record AuthResponse(
            String accessToken,
            String tokenType,
            long expiresInSeconds
    ) {}
}