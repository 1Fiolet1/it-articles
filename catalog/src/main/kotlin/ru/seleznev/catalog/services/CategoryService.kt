package ru.seleznev.catalog.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.seleznev.catalog.application.CategoryReader
import ru.seleznev.catalog.services.repositories.CategoryRepository
import ru.seleznev.catalog.domain.Category
import ru.seleznev.catalog.exceptions.CategoryNotFoundException

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
) : CategoryReader {

    @Transactional(readOnly = true)
    override fun list(): List<Category> {
        return categoryRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Category {
        return categoryRepository.findById(id) ?: throw CategoryNotFoundException(id)
    }
}
