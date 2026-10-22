package ru.seleznev.articles.exceptions

class ArticleVersionConflictException(id: Long) :
    RuntimeException("Article $id was changed by another request; reload it and try again")
