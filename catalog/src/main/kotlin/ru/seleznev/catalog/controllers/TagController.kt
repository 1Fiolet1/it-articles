package ru.seleznev.catalog.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.seleznev.catalog.application.TagReader
import ru.seleznev.catalog.dto.TagResponse
import ru.seleznev.catalog.mappers.TagMapper

@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagReader: TagReader,
    private val tagMapper: TagMapper,
) {

    @GetMapping
    @Operation(summary = "Получить список тегов")
    @ApiResponse(responseCode = "200", description = "Список тегов получен")
    fun list(): List<TagResponse> {
        return tagReader.list().map { tagMapper.toResponse(it) }
    }
}
