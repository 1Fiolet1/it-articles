package ru.seleznev.users.springdata

import org.springframework.data.jpa.repository.JpaRepository
import ru.seleznev.users.entities.UserEntity

interface SpringDataUserRepository : JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): UserEntity?
}
