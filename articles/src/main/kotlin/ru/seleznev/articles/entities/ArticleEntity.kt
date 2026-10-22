package ru.seleznev.articles.entities

import jakarta.persistence.Column
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Entity
@Table(name = "articles")
class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 200)
    var title: String = "",

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = "",

    @Column(name = "author_id", nullable = false)
    var authorId: Long = 0,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long = 0,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "article_tags",
        joinColumns = [JoinColumn(name = "article_id")],
    )
    @Column(name = "tag_id", nullable = false)
    var tagIds: MutableSet<Long> = mutableSetOf(),

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    @Version
    @Column(nullable = false)
    var version: Long = 0,
)
