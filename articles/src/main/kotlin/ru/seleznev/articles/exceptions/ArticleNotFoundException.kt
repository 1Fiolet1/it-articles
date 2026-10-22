package ru.seleznev.articles.exceptions

class ArticleNotFoundException(id: Long) :
    RuntimeException("Article with id $id was not found")
