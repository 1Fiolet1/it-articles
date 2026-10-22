package ru.seleznev.catalog.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.seleznev.catalog.application.CategoryReader
import ru.seleznev.catalog.dto.CategoryResponse
import ru.seleznev.catalog.mappers.CategoryMapper

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryReader: CategoryReader,
    private val categoryMapper: CategoryMapper,
) {

    @GetMapping
    @Operation(summary = "Получить список категорий")
    @ApiResponse(responseCode = "200", description = "Список категорий получен")
    fun list(): List<CategoryResponse> {
        return categoryReader.list().map { categoryMapper.toResponse(it) }
    }
}
