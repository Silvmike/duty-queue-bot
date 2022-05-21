package ru.silvmike.bot.dao.api

import ru.silvmike.bot.model.DutyQueue

interface QueueDao {

    fun get(ownerId: Long): DutyQueue

    fun save(queue: DutyQueue)
}