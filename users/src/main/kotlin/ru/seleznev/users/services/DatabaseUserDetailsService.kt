package ru.seleznev.users.services

import org.springframework.security.core.userdetails.User as SecurityUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.seleznev.users.application.UserReader
import ru.seleznev.users.exceptions.UserNotFoundException

@Service
class DatabaseUserDetailsService(
    private val userReader: UserReader,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return try {
            val user = userReader.getByUsername(username)
            SecurityUser.withUsername(user.username)
                .password(user.passwordHash)
                .roles("USER")
                .build()
        } catch (_: UserNotFoundException) {
            throw UsernameNotFoundException("Invalid username or password")
        }
    }
}
