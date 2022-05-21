package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.util.KMongoUtil
import kotlin.reflect.KClass

abstract class MongoDao<T : Any>(
    private val modelClass: KClass<T>,
    private val client: MongoClient,
    private val dbName: String) {

    protected fun getCollection(): MongoCollection<T> =
        getDatabase().getCollection(KMongoUtil.defaultCollectionName(modelClass), modelClass.java)

    private fun getDatabase(): MongoDatabase {
        return client.getDatabase(dbName)
    }
}