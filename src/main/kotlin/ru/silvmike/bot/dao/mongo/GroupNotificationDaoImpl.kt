package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import org.litote.kmongo.and
import org.litote.kmongo.eq
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.model.GroupNotification

class GroupNotificationDaoImpl(client: MongoClient, dbName: String) :
    MongoDao<GroupNotification>(GroupNotification::class, client, dbName), GroupNotificationDao {

    override fun save(notification: GroupNotification) {
        getCollection().insertOne(notification)
    }

    override fun remove(id: String) {
        getCollection().deleteOne(GroupNotification::id eq id)
    }

    override fun findByOwnerId(ownerId: Long): Sequence<GroupNotification> =
        getCollection().find(GroupNotification::ownerId eq ownerId).asSequence()

    override fun findByOwnerAndChatId(ownerId: Long, chatId: Long): GroupNotification? =
        getCollection().find(
            and(
                GroupNotification::ownerId eq ownerId,
                GroupNotification::chatId eq chatId
            )
        ).firstOrNull()

    override fun findAll(): Sequence<GroupNotification> =
        getCollection().find().asSequence()

}