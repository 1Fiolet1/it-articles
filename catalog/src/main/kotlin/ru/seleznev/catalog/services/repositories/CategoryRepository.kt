package ru.seleznev.catalog.services.repositories

import ru.seleznev.catalog.domain.Category

interface CategoryRepository {
    fun findAll(): List<Category>
    fun findById(id: Long): Category?
}
