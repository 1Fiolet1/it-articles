package ru.seleznev.articles.services.models

data class ArticleFilter(
    val search: String? = null,
    val categoryId: Long? = null,
    val tagIds: Set<Long> = emptySet(),
)
