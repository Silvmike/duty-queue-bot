package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.Token

class TokenDaoImpl(client: MongoClient, dbName: String) : MongoDao<Token>(Token::class, client, dbName), TokenDao {

    override fun save(token: Token) {
        val found = getCollection().findOneAndReplace(
            Token::token eq token.token,
            token
        )
        if (found == null) {
            getCollection().insertOne(token)
        }
    }

    override fun get(token: String): Token? =
        getCollection().findOne { Token::token eq token }

}