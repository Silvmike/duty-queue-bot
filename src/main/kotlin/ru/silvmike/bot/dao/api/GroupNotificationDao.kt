package ru.silvmike.bot.dao.api

import ru.silvmike.bot.model.GroupNotification

interface GroupNotificationDao {

    fun save(groupNotification: GroupNotification)

    fun remove(id: String)

    fun findByOwnerId(ownerId: Long): Sequence<GroupNotification>

    fun findByOwnerAndChatId(ownerId: Long, chatId: Long): GroupNotification?

    fun findAll(): Sequence<GroupNotification>

}