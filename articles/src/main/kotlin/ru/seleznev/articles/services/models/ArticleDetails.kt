package ru.seleznev.articles.services.models

import java.time.Instant

data class ArticleDetails(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorUsername: String,
    val categoryId: Long,
    val categoryName: String,
    val tags: List<ArticleTagDetails>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Long,
)
