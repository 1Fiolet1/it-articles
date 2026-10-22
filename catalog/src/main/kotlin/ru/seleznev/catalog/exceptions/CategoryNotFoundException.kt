package ru.seleznev.catalog.exceptions

class CategoryNotFoundException(id: Long) :
    RuntimeException("Category with id $id was not found")
