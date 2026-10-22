package ru.seleznev.articles.dto

import java.time.Instant

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: ArticleAuthorResponse,
    val category: ArticleCategoryResponse,
    val tags: List<ArticleTagResponse>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Long,
)
