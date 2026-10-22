package ru.seleznev.articles.domain

import java.time.Instant
import ru.seleznev.articles.exceptions.ArticleAccessDeniedException
import ru.seleznev.articles.exceptions.ArticleVersionConflictException

data class Article(
    val id: Long? = null,
    val title: String,
    val content: String,
    val authorId: Long,
    val categoryId: Long,
    val tagIds: Set<Long>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt,
    val version: Long = 0,
) {
    fun update(
        requesterId: Long,
        newTitle: String,
        newContent: String,
        newCategoryId: Long,
        newTagIds: Set<Long>,
        expectedVersion: Long,
        now: Instant = Instant.now(),
    ): Article {
        if (authorId != requesterId) {
            throw ArticleAccessDeniedException(requireNotNull(id))
        }
        if (version != expectedVersion) {
            throw ArticleVersionConflictException(requireNotNull(id))
        }

        return copy(
            title = newTitle,
            content = newContent,
            categoryId = newCategoryId,
            tagIds = newTagIds,
            updatedAt = now,
        )
    }
}
