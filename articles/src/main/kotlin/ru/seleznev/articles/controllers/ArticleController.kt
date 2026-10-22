package ru.seleznev.articles.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import java.security.Principal
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.seleznev.articles.dto.ArticleResponse
import ru.seleznev.articles.dto.ArticleFilterRequest
import ru.seleznev.articles.dto.CreateArticleRequest
import ru.seleznev.articles.dto.UpdateArticleRequest
import ru.seleznev.articles.mappers.ArticleMapper
import ru.seleznev.articles.services.ArticleService

@RestController
@RequestMapping("/api/articles")
@Validated
class ArticleController(
    private val articleService: ArticleService,
    private val articleMapper: ArticleMapper,
) {

    @GetMapping
    @Operation(summary = "Получить список статей с поиском и фильтрацией")
    @ApiResponse(responseCode = "200", description = "Список статей получен")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры фильтрации")
    fun list(@Valid @ModelAttribute request: ArticleFilterRequest): List<ArticleResponse> {
        val filter = articleMapper.toFilter(request)
        return articleService.list(filter).map { articleMapper.toResponse(it) }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить статью")
    @ApiResponse(responseCode = "200", description = "Статья найдена")
    @ApiResponse(responseCode = "404", description = "Статья не найдена")
    fun get(@PathVariable id: Long): ArticleResponse {
        return articleMapper.toResponse(articleService.get(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать статью")
    @ApiResponse(responseCode = "201", description = "Статья создана")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "404", description = "Категория или тег не найдены")
    fun create(
        principal: Principal,
        @Valid @RequestBody request: CreateArticleRequest,
    ): ArticleResponse {
        val command = articleMapper.toCommand(request)
        return articleMapper.toResponse(articleService.create(principal.name, command))
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить статью")
    @ApiResponse(responseCode = "200", description = "Статья изменена")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    @ApiResponse(responseCode = "403", description = "Нельзя изменять чужую статью")
    @ApiResponse(responseCode = "404", description = "Статья, категория или тег не найдены")
    @ApiResponse(responseCode = "409", description = "Статья уже изменена другим запросом")
    fun update(
        @PathVariable id: Long,
        principal: Principal,
        @Valid @RequestBody request: UpdateArticleRequest,
    ): ArticleResponse {
        val command = articleMapper.toCommand(request)
        return articleMapper.toResponse(articleService.update(id, principal.name, command))
    }
}
