package ru.seleznev.articles.exceptions

class ArticleAccessDeniedException(id: Long) :
    RuntimeException("Only the author may edit article $id")
