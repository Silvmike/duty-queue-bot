package ru.silvmike.bot.dao.stub

import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.Token
import java.util.*
import kotlin.collections.HashMap

class TokenDaoImpl : TokenDao {

    private val storage = HashMap<String, Token>()

    override fun get(token: String): Token? = storage[token]

    override fun save(token: Token) {
        token.apply {
            storage[this.token] = this
        }
    }
}