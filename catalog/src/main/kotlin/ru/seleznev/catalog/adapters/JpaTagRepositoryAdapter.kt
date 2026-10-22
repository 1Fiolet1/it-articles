package ru.seleznev.catalog.adapters

import org.springframework.stereotype.Repository
import ru.seleznev.catalog.domain.Tag
import ru.seleznev.catalog.mappers.TagMapper
import ru.seleznev.catalog.services.repositories.TagRepository
import ru.seleznev.catalog.springdata.SpringDataTagRepository

@Repository
class JpaTagRepositoryAdapter(
    private val repository: SpringDataTagRepository,
    private val tagMapper: TagMapper,
) : TagRepository {

    override fun findAll(): List<Tag> {
        return repository.findAll().map { tagMapper.toDomain(it) }
    }

    override fun findAllByIds(ids: Set<Long>): List<Tag> {
        return repository.findAllById(ids).map { tagMapper.toDomain(it) }
    }
}
