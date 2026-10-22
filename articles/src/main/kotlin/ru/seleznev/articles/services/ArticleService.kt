package ru.seleznev.articles.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.seleznev.articles.domain.Article
import ru.seleznev.articles.exceptions.ArticleNotFoundException
import ru.seleznev.articles.mappers.ArticleMapper
import ru.seleznev.articles.services.commands.CreateArticleCommand
import ru.seleznev.articles.services.commands.UpdateArticleCommand
import ru.seleznev.articles.services.models.ArticleDetails
import ru.seleznev.articles.services.models.ArticleFilter
import ru.seleznev.articles.services.repositories.ArticleRepository
import ru.seleznev.catalog.application.CategoryReader
import ru.seleznev.catalog.application.TagReader
import ru.seleznev.users.application.UserReader

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userReader: UserReader,
    private val categoryReader: CategoryReader,
    private val tagReader: TagReader,
    private val articleMapper: ArticleMapper,
) {

    private val logger = LoggerFactory.getLogger(ArticleService::class.java)

    @Transactional
    fun create(username: String, command: CreateArticleCommand): ArticleDetails {
        val author = userReader.getByUsername(username)
        val authorId = requireNotNull(author.id)
        categoryReader.getById(command.categoryId)
        tagReader.getAllByIds(command.tagIds)

        val article = articleRepository.save(
            Article(
                title = command.title.trim(),
                content = command.content.trim(),
                authorId = authorId,
                categoryId = command.categoryId,
                tagIds = command.tagIds,
            ),
        )
        logger.info("Article created: id={}, authorId={}", article.id, article.authorId)
        return articleMapper.toDetails(article)
    }

    @Transactional(readOnly = true)
    fun list(filter: ArticleFilter): List<ArticleDetails> {
        return articleRepository.findAll(filter).map { articleMapper.toDetails(it) }
    }

    @Transactional(readOnly = true)
    fun get(id: Long): ArticleDetails {
        return articleMapper.toDetails(findArticle(id))
    }

    @Transactional(readOnly = true)
    fun listMine(username: String): List<ArticleDetails> {
        val user = userReader.getByUsername(username)
        return articleRepository.findAllByAuthorId(requireNotNull(user.id))
            .map { articleMapper.toDetails(it) }
    }

    @Transactional
    fun update(id: Long, username: String, command: UpdateArticleCommand): ArticleDetails {
        val requester = userReader.getByUsername(username)
        val article = findArticle(id)
        val updated = article.update(
            requesterId = requireNotNull(requester.id),
            newTitle = command.title.trim(),
            newContent = command.content.trim(),
            newCategoryId = command.categoryId,
            newTagIds = command.tagIds,
            expectedVersion = command.version,
        )
        categoryReader.getById(command.categoryId)
        tagReader.getAllByIds(command.tagIds)
        val saved = articleRepository.save(updated)
        logger.info("Article updated: id={}, authorId={}", saved.id, saved.authorId)
        return articleMapper.toDetails(saved)
    }

    private fun findArticle(id: Long): Article {
        return articleRepository.findById(id) ?: throw ArticleNotFoundException(id)
    }
}
