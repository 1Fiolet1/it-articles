package ru.seleznev.users.adapters

import org.springframework.stereotype.Repository
import org.springframework.data.repository.findByIdOrNull
import ru.seleznev.users.services.repositories.UserRepository
import ru.seleznev.users.domain.User
import ru.seleznev.users.mappers.UserMapper
import ru.seleznev.users.springdata.SpringDataUserRepository

@Repository
class JpaUserRepositoryAdapter(
    private val repository: SpringDataUserRepository,
    private val userMapper: UserMapper,
) : UserRepository {

    override fun existsByUsername(username: String): Boolean {
        return repository.existsByUsername(username)
    }

    override fun findByUsername(username: String): User? {
        return repository.findByUsername(username)?.let { userMapper.toDomain(it) }
    }

    override fun findById(id: Long): User? {
        return repository.findByIdOrNull(id)?.let { userMapper.toDomain(it) }
    }

    override fun save(user: User): User {
        val savedEntity = repository.save(userMapper.toEntity(user))
        return userMapper.toDomain(savedEntity)
    }
}
