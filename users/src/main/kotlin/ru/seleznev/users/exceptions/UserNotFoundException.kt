package ru.seleznev.users.exceptions

class UserNotFoundException(username: String) :
    RuntimeException("User '$username' was not found")
