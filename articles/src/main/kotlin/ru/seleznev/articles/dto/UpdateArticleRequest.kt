package ru.seleznev.articles.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

data class UpdateArticleRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 3, max = 200, message = "Title must contain from 3 to 200 characters")
    val title: String,

    @field:NotBlank(message = "Content is required")
    @field:Size(min = 10, message = "Content must contain at least 10 characters")
    val content: String,

    @field:Positive(message = "Category id must be positive")
    val categoryId: Long,

    @field:NotEmpty(message = "At least one tag is required")
    @field:Size(max = 20, message = "No more than 20 tags can be selected")
    val tagIds: Set<@Positive(message = "Tag id must be positive") Long>,

    @field:PositiveOrZero(message = "Version must not be negative")
    val version: Long,
)
