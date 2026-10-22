package ru.seleznev.catalog.mappers

import org.springframework.stereotype.Component
import ru.seleznev.catalog.domain.Tag
import ru.seleznev.catalog.dto.TagResponse
import ru.seleznev.catalog.entities.TagEntity

@Component
class TagMapper {
    fun toResponse(tag: Tag): TagResponse {
        return TagResponse(
            id = requireNotNull(tag.id),
            name = tag.name,
            slug = tag.slug,
        )
    }

    fun toDomain(entity: TagEntity): Tag {
        return Tag(
            id = entity.id,
            name = entity.name,
            slug = entity.slug,
        )
    }
}
