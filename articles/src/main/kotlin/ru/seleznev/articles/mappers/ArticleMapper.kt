package ru.seleznev.articles.mappers

import org.springframework.stereotype.Component
import ru.seleznev.articles.domain.Article
import ru.seleznev.articles.dto.ArticleAuthorResponse
import ru.seleznev.articles.dto.ArticleCategoryResponse
import ru.seleznev.articles.dto.ArticleFilterRequest
import ru.seleznev.articles.dto.ArticleResponse
import ru.seleznev.articles.dto.ArticleTagResponse
import ru.seleznev.articles.dto.CreateArticleRequest
import ru.seleznev.articles.dto.UpdateArticleRequest
import ru.seleznev.articles.entities.ArticleEntity
import ru.seleznev.articles.services.commands.CreateArticleCommand
import ru.seleznev.articles.services.commands.UpdateArticleCommand
import ru.seleznev.articles.services.models.ArticleDetails
import ru.seleznev.articles.services.models.ArticleFilter
import ru.seleznev.articles.services.models.ArticleTagDetails
import ru.seleznev.catalog.application.CategoryReader
import ru.seleznev.catalog.application.TagReader
import ru.seleznev.users.application.UserReader

@Component
class ArticleMapper(
    private val userReader: UserReader,
    private val categoryReader: CategoryReader,
    private val tagReader: TagReader,
) {
    fun toCommand(request: CreateArticleRequest): CreateArticleCommand {
        return CreateArticleCommand(request.title, request.content, request.categoryId, request.tagIds)
    }

    fun toCommand(request: UpdateArticleRequest): UpdateArticleCommand {
        return UpdateArticleCommand(
            request.title,
            request.content,
            request.categoryId,
            request.tagIds,
            request.version,
        )
    }

    fun toFilter(request: ArticleFilterRequest): ArticleFilter {
        return ArticleFilter(
            search = request.search?.trim()?.takeIf { it.isNotEmpty() },
            categoryId = request.categoryId,
            tagIds = request.tagIds,
        )
    }

    fun toEntity(article: Article): ArticleEntity {
        return ArticleEntity(
            id = article.id,
            title = article.title,
            content = article.content,
            authorId = article.authorId,
            categoryId = article.categoryId,
            tagIds = article.tagIds.toMutableSet(),
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            version = article.version,
        )
    }

    fun toDomain(entity: ArticleEntity): Article {
        return Article(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            authorId = entity.authorId,
            categoryId = entity.categoryId,
            tagIds = entity.tagIds.toSet(),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            version = entity.version,
        )
    }

    fun toResponse(details: ArticleDetails): ArticleResponse {
        return ArticleResponse(
            id = details.id,
            title = details.title,
            content = details.content,
            author = ArticleAuthorResponse(details.authorId, details.authorUsername),
            category = ArticleCategoryResponse(details.categoryId, details.categoryName),
            tags = details.tags.map { ArticleTagResponse(it.id, it.name) },
            createdAt = details.createdAt,
            updatedAt = details.updatedAt,
            version = details.version,
        )
    }

    fun toDetails(article: Article): ArticleDetails {
        val author = userReader.getById(article.authorId)
        val category = categoryReader.getById(article.categoryId)
        val tags = tagReader.getAllByIds(article.tagIds)
        return ArticleDetails(
            id = requireNotNull(article.id),
            title = article.title,
            content = article.content,
            authorId = article.authorId,
            authorUsername = author.username,
            categoryId = article.categoryId,
            categoryName = category.name,
            tags = tags.map { tag ->
                ArticleTagDetails(
                    id = requireNotNull(tag.id),
                    name = tag.name,
                )
            },
            createdAt = article.createdAt,
            updatedAt = article.updatedAt,
            version = article.version,
        )
    }
}
