package ru.seleznev.users.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must contain from 3 to 50 characters")
    @field:Pattern(
        regexp = "^[A-Za-z0-9_.-]+$",
        message = "Username may contain letters, digits, dot, underscore and hyphen",
    )
    val username: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 72, message = "Password must contain from 8 to 72 characters")
    val password: String,
)
