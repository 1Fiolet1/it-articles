package ru.seleznev.users.services

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.seleznev.users.application.UserReader
import ru.seleznev.users.services.repositories.UserRepository
import ru.seleznev.users.domain.User
import ru.seleznev.users.exceptions.UserNotFoundException
import ru.seleznev.users.exceptions.UsernameAlreadyExistsException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserReader {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun register(username: String, password: String): User {
        val normalizedUsername = normalize(username)
        if (userRepository.existsByUsername(normalizedUsername)) {
            logger.warn("Registration rejected: username={} already exists", normalizedUsername)
            throw UsernameAlreadyExistsException(normalizedUsername)
        }

        val saved = userRepository.save(
            User(
                username = normalizedUsername,
                passwordHash = requireNotNull(passwordEncoder.encode(password)),
            ),
        )
        logger.info("User registered: id={}, username={}", saved.id, saved.username)
        return saved
    }

    @Transactional(readOnly = true)
    override fun getByUsername(username: String): User {
        return userRepository.findByUsername(normalize(username))
            ?: throw UserNotFoundException(username)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): User {
        return userRepository.findById(id) ?: throw UserNotFoundException("id=$id")
    }

    private fun normalize(username: String): String {
        return username.trim().lowercase()
    }
}
