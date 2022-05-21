package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

class UserDaoImpl(client: MongoClient, dbName: String) : MongoDao<User>(User::class, client, dbName), UserDao {

    override fun save(user: User) {
        getCollection().insertOne(user)
    }

    override fun findByUsername(username: String): User? =
        getCollection().findOne { User::username eq username }

    override fun findById(id: Long): User? =
        getCollection().findOne { User::id eq id }

    override fun findAll(): Sequence<User> = getCollection().find().asSequence()

}