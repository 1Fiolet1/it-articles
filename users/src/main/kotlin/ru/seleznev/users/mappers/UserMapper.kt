package ru.seleznev.users.mappers

import org.springframework.stereotype.Component
import ru.seleznev.users.domain.User
import ru.seleznev.users.dto.UserResponse
import ru.seleznev.users.entities.UserEntity

@Component
class UserMapper {
    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = requireNotNull(user.id),
            username = user.username,
            createdAt = user.createdAt,
        )
    }

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            username = user.username,
            passwordHash = user.passwordHash,
            createdAt = user.createdAt,
        )
    }

    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            passwordHash = entity.passwordHash,
            createdAt = entity.createdAt,
        )
    }
}
