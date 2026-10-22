package ru.seleznev.articles.adapters

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import ru.seleznev.articles.domain.Article
import ru.seleznev.articles.mappers.ArticleMapper
import ru.seleznev.articles.services.models.ArticleFilter
import ru.seleznev.articles.services.repositories.ArticleRepository
import ru.seleznev.articles.springdata.SpringDataArticleRepository

@Repository
class JpaArticleRepositoryAdapter(
    private val repository: SpringDataArticleRepository,
    private val articleMapper: ArticleMapper,
) : ArticleRepository {

    override fun save(article: Article): Article {
        val savedEntity = repository.saveAndFlush(articleMapper.toEntity(article))
        return articleMapper.toDomain(savedEntity)
    }

    override fun findById(id: Long): Article? {
        return repository.findByIdOrNull(id)?.let { articleMapper.toDomain(it) }
    }

    override fun findAll(filter: ArticleFilter): List<Article> {
        return repository.findAll()
            .filter { entity ->
                filter.search == null ||
                    entity.title.contains(filter.search, ignoreCase = true) ||
                    entity.content.contains(filter.search, ignoreCase = true)
            }
            .filter { entity ->
                filter.categoryId == null || entity.categoryId == filter.categoryId
            }
            .filter { entity ->
                filter.tagIds.isEmpty() || entity.tagIds.any { tagId -> tagId in filter.tagIds }
            }
            .sortedByDescending { entity -> entity.createdAt }
            .map { entity -> articleMapper.toDomain(entity) }
    }

    override fun findAllByAuthorId(authorId: Long): List<Article> {
        return repository.findAllByAuthorIdOrderByCreatedAtDesc(authorId).map { articleMapper.toDomain(it) }
    }
}
