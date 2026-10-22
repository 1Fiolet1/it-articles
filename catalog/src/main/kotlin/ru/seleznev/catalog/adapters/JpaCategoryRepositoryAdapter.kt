package ru.seleznev.catalog.adapters

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import ru.seleznev.catalog.services.repositories.CategoryRepository
import ru.seleznev.catalog.domain.Category
import ru.seleznev.catalog.mappers.CategoryMapper
import ru.seleznev.catalog.springdata.SpringDataCategoryRepository

@Repository
class JpaCategoryRepositoryAdapter(
    private val repository: SpringDataCategoryRepository,
    private val categoryMapper: CategoryMapper,
    ) : CategoryRepository {

    override fun findAll(): List<Category> {
        return repository.findAll().map { categoryMapper.toDomain(it) }
    }

    override fun findById(id: Long): Category? {
        return repository.findByIdOrNull(id)?.let { categoryMapper.toDomain(it) }
    }
}
