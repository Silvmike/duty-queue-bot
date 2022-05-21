package ru.silvmike.bot.dao.api

import ru.silvmike.bot.model.Token

interface TokenDao {

    fun save(token: Token)

    fun get(token: String): Token?

}