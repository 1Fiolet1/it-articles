package ru.seleznev.articles.services.commands

data class CreateArticleCommand(
    val title: String,
    val content: String,
    val categoryId: Long,
    val tagIds: Set<Long>,
)
