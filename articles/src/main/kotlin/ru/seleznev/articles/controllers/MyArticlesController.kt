package ru.seleznev.articles.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.security.Principal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.seleznev.articles.dto.ArticleResponse
import ru.seleznev.articles.mappers.ArticleMapper
import ru.seleznev.articles.services.ArticleService

@RestController
@RequestMapping("/api/me/articles")
class MyArticlesController(
    private val articleService: ArticleService,
    private val articleMapper: ArticleMapper,
) {

    @GetMapping
    @Operation(summary = "Получить свои статьи")
    @ApiResponse(responseCode = "200", description = "Список статей пользователя получен")
    @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    fun listMine(principal: Principal): List<ArticleResponse> {
        return articleService.listMine(principal.name).map { articleMapper.toResponse(it) }
    }
}
