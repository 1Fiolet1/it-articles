package ru.seleznev.users.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Answers
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.stubbing.Answer
import org.springframework.security.crypto.password.PasswordEncoder
import ru.seleznev.users.services.repositories.UserRepository
import ru.seleznev.users.domain.User
import ru.seleznev.users.exceptions.UsernameAlreadyExistsException

class UserServiceTest {

    private val repository = mock(
        UserRepository::class.java,
        Answer { invocation ->
            if (invocation.method.name == "save") {
                invocation.getArgument<User>(0).copy(id = 1L)
            } else {
                Answers.RETURNS_DEFAULTS.answer(invocation)
            }
        },
    )
    private val encoder = mock(PasswordEncoder::class.java)
    private val service = UserService(repository, encoder)

    @Test
    fun `registration normalizes username and encodes password`() {
        doReturn(false).`when`(repository).existsByUsername("alice")
        doReturn("encoded-password").`when`(encoder).encode("password123")

        val result = service.register("  Alice  ", "password123")

        assertEquals("alice", result.username)
        assertEquals("encoded-password", result.passwordHash)
        verify(encoder).encode("password123")
        verify(repository).existsByUsername("alice")
    }

    @Test
    fun `duplicate username is rejected`() {
        doReturn(true).`when`(repository).existsByUsername("alice")

        assertFailsWith<UsernameAlreadyExistsException> {
            service.register("ALICE", "anotherPassword")
        }

        verify(encoder, never()).encode("anotherPassword")
    }
}
