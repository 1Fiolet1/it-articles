package ru.seleznev.catalog.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.seleznev.catalog.application.TagReader
import ru.seleznev.catalog.domain.Tag
import ru.seleznev.catalog.exceptions.TagNotFoundException
import ru.seleznev.catalog.services.repositories.TagRepository

@Service
class TagService(
    private val tagRepository: TagRepository,
) : TagReader {

    @Transactional(readOnly = true)
    override fun list(): List<Tag> {
        return tagRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun getAllByIds(ids: Set<Long>): List<Tag> {
        if (ids.isEmpty()) {
            return emptyList()
        }

        val tags = tagRepository.findAllByIds(ids)
        val foundIds = tags.mapNotNull { it.id }.toSet()
        val missingId = ids.firstOrNull { it !in foundIds }
        if (missingId != null) {
            throw TagNotFoundException(missingId)
        }

        return tags.sortedBy { it.name }
    }
}
