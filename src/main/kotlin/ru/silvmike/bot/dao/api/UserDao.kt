package ru.silvmike.bot.dao.api

import ru.silvmike.bot.model.User

interface UserDao {

    fun save(user: User)

    fun findByUsername(username: String): User?

    fun findById(id: Long): User?

    fun findAll(): Sequence<User>

}