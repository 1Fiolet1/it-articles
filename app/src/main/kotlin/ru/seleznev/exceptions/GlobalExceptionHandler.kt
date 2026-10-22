package ru.seleznev.exceptions

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.seleznev.articles.exceptions.ArticleAccessDeniedException
import ru.seleznev.articles.exceptions.ArticleNotFoundException
import ru.seleznev.articles.exceptions.ArticleVersionConflictException
import ru.seleznev.catalog.exceptions.CategoryNotFoundException
import ru.seleznev.catalog.exceptions.TagNotFoundException
import ru.seleznev.dto.ErrorResponse
import ru.seleznev.users.exceptions.UserNotFoundException
import ru.seleznev.users.exceptions.UsernameAlreadyExistsException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(UsernameAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUsernameAlreadyExists(exception: UsernameAlreadyExistsException): ErrorResponse {
        return ErrorResponse(exception.message ?: "Username is already taken")
    }

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFound(exception: UserNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message ?: "User was not found")
    }

    @ExceptionHandler(CategoryNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleCategoryNotFound(exception: CategoryNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message ?: "Category was not found")
    }

    @ExceptionHandler(TagNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleTagNotFound(exception: TagNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message ?: "Tag was not found")
    }

    @ExceptionHandler(ArticleNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleArticleNotFound(exception: ArticleNotFoundException): ErrorResponse {
        return ErrorResponse(exception.message ?: "Article was not found")
    }

    @ExceptionHandler(ArticleAccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleArticleAccessDenied(exception: ArticleAccessDeniedException): ErrorResponse {
        logger.warn("Article update denied: {}", exception.message)
        return ErrorResponse(exception.message ?: "Access is denied")
    }

    @ExceptionHandler(ArticleVersionConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleArticleVersionConflict(exception: ArticleVersionConflictException): ErrorResponse {
        logger.warn("Article version conflict: {}", exception.message)
        return ErrorResponse(exception.message ?: "Article was changed by another request")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(exception: MethodArgumentNotValidException): ErrorResponse {
        val message = exception.bindingResult.fieldErrors.joinToString("; ") { error ->
            "${error.field}: ${error.defaultMessage ?: "Invalid value"}"
        }
        return ErrorResponse(message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolation(exception: ConstraintViolationException): ErrorResponse {
        return ErrorResponse(exception.message ?: "Request validation failed")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleUnreadableBody(_exception: HttpMessageNotReadableException): ErrorResponse {
        return ErrorResponse("Request body is missing or malformed")
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMismatch(exception: MethodArgumentTypeMismatchException): ErrorResponse {
        return ErrorResponse("Invalid value for '${exception.name}'")
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDataIntegrityViolation(_exception: DataIntegrityViolationException): ErrorResponse {
        return ErrorResponse("Data conflict")
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleOptimisticLocking(_exception: ObjectOptimisticLockingFailureException): ErrorResponse {
        return ErrorResponse("The resource was changed by another request")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnexpected(exception: Exception): ErrorResponse {
        logger.error("Unexpected server error", exception)
        return ErrorResponse("Unexpected server error")
    }
}
