package ru.seleznev.articles.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Answers
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.stubbing.Answer
import ru.seleznev.articles.services.repositories.ArticleRepository
import ru.seleznev.articles.domain.Article
import ru.seleznev.articles.exceptions.ArticleAccessDeniedException
import ru.seleznev.articles.exceptions.ArticleVersionConflictException
import ru.seleznev.articles.mappers.ArticleMapper
import ru.seleznev.articles.services.commands.CreateArticleCommand
import ru.seleznev.articles.services.commands.UpdateArticleCommand
import ru.seleznev.articles.services.models.ArticleDetails
import ru.seleznev.articles.services.models.ArticleTagDetails
import ru.seleznev.catalog.application.CategoryReader
import ru.seleznev.catalog.application.TagReader
import ru.seleznev.catalog.domain.Category
import ru.seleznev.catalog.domain.Tag
import ru.seleznev.users.application.UserReader
import ru.seleznev.users.domain.User

class ArticleServiceTest {

    private val alice = User(id = 1, username = "alice", passwordHash = "hash")
    private val bob = User(id = 2, username = "bob", passwordHash = "hash")
    private val category = Category(id = 10, name = "Backend", slug = "backend")
    private val tag = Tag(id = 20, name = "Kotlin", slug = "kotlin")
    private val userReader = mock(UserReader::class.java)
    private val categoryReader = mock(CategoryReader::class.java)
    private val tagReader = mock(TagReader::class.java)
    private var savedArticle: Article? = null
    private val repository = mock(
        ArticleRepository::class.java,
        Answer { invocation ->
            if (invocation.method.name == "save") {
                val article = invocation.getArgument<Article>(0)
                savedArticle = article
                article.copy(id = 100L)
            } else {
                Answers.RETURNS_DEFAULTS.answer(invocation)
            }
        },
    )
    private val mapper = mock(
        ArticleMapper::class.java,
        Answer { invocation ->
            if (invocation.method.name == "toDetails") {
                val article = invocation.getArgument<Article>(0)
                ArticleDetails(
                    id = requireNotNull(article.id),
                    title = article.title,
                    content = article.content,
                    authorId = article.authorId,
                    authorUsername = "alice",
                    categoryId = article.categoryId,
                    categoryName = "Backend",
                    tags = listOf(ArticleTagDetails(20, "Kotlin")),
                    createdAt = article.createdAt,
                    updatedAt = article.updatedAt,
                    version = article.version,
                )
            } else {
                Answers.RETURNS_DEFAULTS.answer(invocation)
            }
        },
    )
    private val service = ArticleService(repository, userReader, categoryReader, tagReader, mapper)

    @Test
    fun `authenticated user becomes article author`() {
        doReturn(alice).`when`(userReader).getByUsername("alice")
        doReturn(category).`when`(categoryReader).getById(10)
        doReturn(listOf(tag)).`when`(tagReader).getAllByIds(setOf(20))

        val created = service.create(
            username = "alice",
            command = CreateArticleCommand(
                title = "  Kotlin and Spring  ",
                content = "  Meaningful article content  ",
                categoryId = 10,
                tagIds = setOf(20),
            ),
        )

        assertEquals(1, savedArticle?.authorId)
        assertEquals("Kotlin and Spring", savedArticle?.title)
        assertEquals("Meaningful article content", savedArticle?.content)
        assertEquals(setOf(20L), savedArticle?.tagIds)
        assertEquals(1, created.authorId)
        assertEquals("alice", created.authorUsername)
        assertEquals("Backend", created.categoryName)
    }

    @Test
    fun `user cannot update another author's article`() {
        val article = Article(
            id = 100,
            title = "Original title",
            content = "Meaningful article content",
            authorId = 1,
            categoryId = 10,
            tagIds = setOf(20),
        )
        doReturn(bob).`when`(userReader).getByUsername("bob")
        doReturn(category).`when`(categoryReader).getById(10)
        doReturn(article).`when`(repository).findById(100)

        assertFailsWith<ArticleAccessDeniedException> {
            service.update(
                id = 100,
                username = "bob",
                command = UpdateArticleCommand(
                    title = "Changed title",
                    content = "Changed article content",
                    categoryId = 10,
                    tagIds = setOf(20),
                    version = 0,
                ),
            )
        }

        assertEquals(null, savedArticle)
    }

    @Test
    fun `stale article version is rejected`() {
        val article = Article(
            id = 100,
            title = "Original title",
            content = "Meaningful article content",
            authorId = 1,
            categoryId = 10,
            tagIds = setOf(20),
            version = 0,
        )
        doReturn(alice).`when`(userReader).getByUsername("alice")
        doReturn(category).`when`(categoryReader).getById(10)
        doReturn(article).`when`(repository).findById(100)

        assertFailsWith<ArticleVersionConflictException> {
            service.update(
                id = 100,
                username = "alice",
                command = UpdateArticleCommand(
                    title = "Changed title",
                    content = "Changed article content",
                    categoryId = 10,
                    tagIds = setOf(20),
                    version = 99,
                ),
            )
        }

        assertEquals(null, savedArticle)
    }
}
