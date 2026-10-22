package ru.seleznev.users.domain

import java.time.Instant

data class User(
    val id: Long? = null,
    val username: String,
    val passwordHash: String,
    val createdAt: Instant = Instant.now(),
)
