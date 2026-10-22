package ru.seleznev.articles.services.commands

data class UpdateArticleCommand(
    val title: String,
    val content: String,
    val categoryId: Long,
    val tagIds: Set<Long>,
    val version: Long,
)
