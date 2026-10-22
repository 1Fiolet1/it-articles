package ru.seleznev.catalog.application

import ru.seleznev.catalog.domain.Category

interface CategoryReader {
    fun list(): List<Category>
    fun getById(id: Long): Category
}
