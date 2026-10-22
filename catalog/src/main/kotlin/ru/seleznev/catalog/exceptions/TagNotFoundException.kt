package ru.seleznev.catalog.exceptions

class TagNotFoundException(id: Long) :
    RuntimeException("Tag with id $id was not found")
