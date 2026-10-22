package ru.seleznev.catalog.application

import ru.seleznev.catalog.domain.Tag

interface TagReader {
    fun list(): List<Tag>
    fun getAllByIds(ids: Set<Long>): List<Tag>
}
