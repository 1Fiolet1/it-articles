package ru.seleznev.catalog.mappers

import org.springframework.stereotype.Component
import ru.seleznev.catalog.domain.Category
import ru.seleznev.catalog.dto.CategoryResponse
import ru.seleznev.catalog.entities.CategoryEntity

@Component
class CategoryMapper {
    fun toResponse(category: Category): CategoryResponse {
        return CategoryResponse(
            id = requireNotNull(category.id),
            name = category.name,
            slug = category.slug,
        )
    }

    fun toDomain(entity: CategoryEntity): Category {
        return Category(
            id = entity.id,
            name = entity.name,
            slug = entity.slug,
        )
    }
}
