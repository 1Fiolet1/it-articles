package ru.seleznev.users.services.repositories

import ru.seleznev.users.domain.User

interface UserRepository {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
    fun findById(id: Long): User?
    fun save(user: User): User
}
