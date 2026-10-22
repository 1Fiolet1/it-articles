package ru.seleznev.users.dto

import java.time.Instant

data class UserResponse(
    val id: Long,
    val username: String,
    val createdAt: Instant,
)
