package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.User
import java.util.*

class RegisterCommand(
    val tokenDao: TokenDao,
    val userDao: UserDao,
    val queueDao: QueueDao
): Command {

    override fun execute(responder: Responder, message: Message, arguments: List<String>) {

        val userId = message.from!!.id
        val token = arguments.getOrNull(0) ?: "INVALID TOKEN"
        val found = tokenDao.get(token)

        if (found != null && found.usedBy == null) {

            found.usedBy = userId;
            found.usedAt = Date()
            tokenDao.save(found)
            if (userDao.findById(userId) == null) {
                val user = User(id = userId, username = message.from!!.username!!, roles = mutableSetOf(found.role))
                userDao.save(user)
            }

            if (found.creatorId != null) {
                val dutyQueue = queueDao.get(found.creatorId)
                val newQueue = dutyQueue.queue.toMutableList()
                if (!newQueue.contains(userId)) {
                    newQueue.add(userId)
                    queueDao.save(
                        DutyQueue(found.creatorId, newQueue)
                    )
                }
            }

            responder.respond(text = "OK!")
        } else {
            responder.respond(text = "Your token is invalid!")
        }
    }
}