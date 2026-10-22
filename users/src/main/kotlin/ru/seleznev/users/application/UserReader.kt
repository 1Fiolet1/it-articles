package ru.seleznev.users.application

import ru.seleznev.users.domain.User

interface UserReader {
    fun getByUsername(username: String): User
    fun getById(id: Long): User
}
