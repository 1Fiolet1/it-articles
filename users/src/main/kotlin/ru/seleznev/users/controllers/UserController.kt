package ru.seleznev.users.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.seleznev.users.dto.RegisterUserRequest
import ru.seleznev.users.dto.UserResponse
import ru.seleznev.users.mappers.UserMapper
import ru.seleznev.users.services.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Зарегистрировать пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    @ApiResponse(responseCode = "409", description = "Имя пользователя уже занято")
    fun register(@Valid @RequestBody request: RegisterUserRequest): UserResponse {
        val user = userService.register(request.username, request.password)
        return userMapper.toResponse(user)
    }
}
