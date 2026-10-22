package ru.seleznev.users.exceptions

class UsernameAlreadyExistsException(username: String) :
    RuntimeException("Username '$username' is already taken")
