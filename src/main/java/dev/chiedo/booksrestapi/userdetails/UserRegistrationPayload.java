package dev.chiedo.booksrestapi.userdetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationPayload(@NotBlank @Size(min = 0, max = 20) String username,
                                      @NotBlank @Size(min = 0, max = 20) String password,
                                      @NotBlank @Size(min = 0, max = 10) String authority) {
}
