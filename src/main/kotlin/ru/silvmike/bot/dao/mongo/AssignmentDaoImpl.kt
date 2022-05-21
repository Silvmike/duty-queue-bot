package ru.silvmike.bot.dao.mongo

import com.mongodb.client.MongoClient
import org.litote.kmongo.descendingSort
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.Token

class AssignmentDaoImpl(client: MongoClient, dbName: String) :
    MongoDao<Assignment>(Assignment::class, client, dbName), AssignmentDao {

    override fun save(assignment: Assignment) {
        getCollection().insertOne(assignment)
    }

    override fun findLastByAssigneeId(assigneeId: Long, count: Int): Sequence<Assignment> =
        getCollection().find(Assignment::assigneeId eq assigneeId)
            .descendingSort(Assignment::createdAt).limit(count)
            .asSequence()

}