package ru.silvmike.bot.dao.stub

import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.model.GroupNotification

class GroupNotificationDaoImpl : GroupNotificationDao {

    private val storage = HashMap<String, GroupNotification>()
    private val byOwnerIndex = HashMap<Long, Set<GroupNotification>>()

    override fun save(groupNotification: GroupNotification) {
        storage[groupNotification.id] = groupNotification
        val ownerNotifications = byOwnerIndex.computeIfAbsent(groupNotification.ownerId) {
            HashSet()
        } as MutableSet
        ownerNotifications.add(groupNotification)
    }

    override fun remove(id: String) {
        storage.remove(id)
    }

    override fun findByOwnerId(ownerId: Long) =
        byOwnerIndex
            .computeIfAbsent(ownerId) { HashSet() }
            .asSequence()
            .filter { storage.containsKey(it.id) }

    override fun findByOwnerAndChatId(ownerId: Long, chatId: Long): GroupNotification? =
        findByOwnerId(ownerId)
            .filter { it.chatId == chatId }
            .firstOrNull()

    override fun findAll() = storage.values.asSequence()

}