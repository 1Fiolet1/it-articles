package ru.seleznev.articles.springdata

import org.springframework.data.jpa.repository.JpaRepository
import ru.seleznev.articles.entities.ArticleEntity

interface SpringDataArticleRepository : JpaRepository<ArticleEntity, Long> {

    fun findAllByAuthorIdOrderByCreatedAtDesc(authorId: Long): List<ArticleEntity>
}
