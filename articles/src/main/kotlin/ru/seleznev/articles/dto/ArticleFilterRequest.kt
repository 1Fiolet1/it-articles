package ru.seleznev.articles.dto

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ArticleFilterRequest(
    @field:Size(max = 200, message = "Search query must contain at most 200 characters")
    val search: String? = null,

    @field:Positive(message = "Category id must be positive")
    val categoryId: Long? = null,

    @field:Size(max = 20, message = "No more than 20 tags can be selected")
    val tagIds: Set<@Positive(message = "Tag id must be positive") Long> = emptySet(),
)
