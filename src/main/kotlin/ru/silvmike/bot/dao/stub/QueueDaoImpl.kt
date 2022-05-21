package ru.silvmike.bot.dao.stub

import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.model.DutyQueue

class QueueDaoImpl: QueueDao {

    private val storage = HashMap<Long, DutyQueue>()

    override fun get(ownerId: Long): DutyQueue = storage[ownerId] ?: DutyQueue(ownerId, listOf())

    override fun save(queue: DutyQueue) {
        storage[queue.ownerId] = queue
    }
}