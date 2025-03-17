package dev.chiedo.bookapi.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(@NotBlank @Size(max = 20) String username,
                                  @NotBlank @Size(max = 20) String password,
                                  @NotBlank @Size(max = 10) String authority) {
}
