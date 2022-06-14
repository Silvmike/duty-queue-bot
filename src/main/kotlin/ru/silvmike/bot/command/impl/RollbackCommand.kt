package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue

class RollbackCommand(
    private val queueDao: QueueDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        if (isAdmin(roles)) {

            val userId = message.from!!.id
            val dutyQueue = queueDao.get(userId)

            if (dutyQueue.queue.isNotEmpty()) {

                val queue = dutyQueue.queue.toMutableList()
                val assigneeId = queue.removeLast()
                queue.add(0, assigneeId)

                queueDao.save(DutyQueue(ownerId = userId, queue = queue))

                responder.respond(text = "Колесо было откачено на один шаг.")
            }
        }
    }
}