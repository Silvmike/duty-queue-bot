package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.Token

class QueueDaoImpl(client: MongoClient, dbName: String) :
    MongoDao<DutyQueue>(DutyQueue::class, client, dbName), QueueDao {

    override fun get(ownerId: Long): DutyQueue = get(ownerId, 1)

    private fun get(ownerId: Long, attempts: Int): DutyQueue {
        if (attempts < 0) throw RuntimeException("Attempts limit exceeded")

        val queue: DutyQueue? = getCollection().findOne { DutyQueue::ownerId eq ownerId }
        if (queue == null) {
            try {
                save(DutyQueue(ownerId, mutableListOf(), mutableSetOf()))
            } catch (e : Exception) {
                // omit
            }
            return get(ownerId, attempts - 1)
        } else {
            return queue
        }
    }

    override fun save(queue: DutyQueue) {
        val result = getCollection().findOneAndReplace(
            DutyQueue::ownerId eq queue.ownerId,
            queue
        )
        if (result == null) {
            getCollection().insertOne(queue)
        }
    }

}