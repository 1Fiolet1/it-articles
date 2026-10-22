package ru.seleznev.catalog.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import ru.seleznev.catalog.domain.Tag
import ru.seleznev.catalog.exceptions.TagNotFoundException
import ru.seleznev.catalog.services.repositories.TagRepository

class TagServiceTest {

    private val repository = mock(TagRepository::class.java)
    private val service = TagService(repository)

    @Test
    fun `all requested tags are returned`() {
        val kotlinTag = Tag(id = 1, name = "Kotlin", slug = "kotlin")
        val springTag = Tag(id = 2, name = "Spring", slug = "spring")
        doReturn(listOf(springTag, kotlinTag)).`when`(repository).findAllByIds(setOf(1, 2))

        val result = service.getAllByIds(setOf(1, 2))

        assertEquals(listOf(kotlinTag, springTag), result)
    }

    @Test
    fun `missing requested tag is rejected`() {
        val kotlinTag = Tag(id = 1, name = "Kotlin", slug = "kotlin")
        doReturn(listOf(kotlinTag)).`when`(repository).findAllByIds(setOf(1, 2))

        assertFailsWith<TagNotFoundException> {
            service.getAllByIds(setOf(1, 2))
        }
    }
}
