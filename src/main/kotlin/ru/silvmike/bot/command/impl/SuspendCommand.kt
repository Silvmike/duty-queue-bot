package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.User
import java.util.*

class SuspendCommand(
    val userDao: UserDao,
    val queueDao: QueueDao,
    val authService: AuthService
): AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        val userId = message.from!!.id

        if (isAdmin(roles)) {

            if (arguments.isNotEmpty()) {

                val username = arguments[0].removePrefix("@")

                val targetUserId = userDao.findByUsername(username)?.id
                if (targetUserId != null) {

                    queueDao.get(userId).apply {

                        if (suspended!!.contains(targetUserId)) {
                            responder.respond("Участие пользователя @$username в очереди дежурств уже приостановлено.")
                        } else if (!queue.contains(targetUserId)) {
                            responder.respond("Пользователь @$username не принимает участие в очереди дежурств.")
                        } else {

                            queueDao.save(
                                DutyQueue(
                                    ownerId = userId,
                                    queue = queue.asSequence().filter { it != targetUserId }.toList(),
                                    suspended = if (suspended != null) {
                                        suspended!!.plus(targetUserId)
                                    } else {
                                        setOf(targetUserId)
                                    }
                                )
                            )

                            responder.respond("Участие пользователя @$username в очереди дежурств приостановлено.")
                        }
                    }
                } else {

                    responder.respond("Не удалось найти пользователя @$username.")
                }
            } else {
                responder.respond("Синтаксис /suspend @username.")
            }
        }
    }
}