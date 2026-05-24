package com.umograd.presentation.parent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateChildRequest(
        @NotBlank String username,
        @Email String email,
        @Size(min = 5) String password
) {}
