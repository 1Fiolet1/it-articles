package ru.seleznev.catalog.services.repositories

import ru.seleznev.catalog.domain.Tag

interface TagRepository {
    fun findAll(): List<Tag>
    fun findAllByIds(ids: Set<Long>): List<Tag>
}
