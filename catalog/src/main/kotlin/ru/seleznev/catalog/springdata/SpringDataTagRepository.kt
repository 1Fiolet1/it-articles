package ru.seleznev.catalog.springdata

import org.springframework.data.jpa.repository.JpaRepository
import ru.seleznev.catalog.entities.TagEntity

interface SpringDataTagRepository : JpaRepository<TagEntity, Long>
