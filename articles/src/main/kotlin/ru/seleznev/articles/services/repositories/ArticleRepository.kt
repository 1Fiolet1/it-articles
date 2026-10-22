package ru.seleznev.articles.services.repositories

import ru.seleznev.articles.domain.Article
import ru.seleznev.articles.services.models.ArticleFilter

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
    fun findAll(filter: ArticleFilter): List<Article>
    fun findAllByAuthorId(authorId: Long): List<Article>
}
