package ru.seleznev.catalog.springdata

import org.springframework.data.jpa.repository.JpaRepository
import ru.seleznev.catalog.entities.CategoryEntity

interface SpringDataCategoryRepository : JpaRepository<CategoryEntity, Long>
