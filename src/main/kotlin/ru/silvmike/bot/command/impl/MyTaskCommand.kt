package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.AssignmentDao
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class MyTaskCommand(
    private val assignmentDao: AssignmentDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        if (canGetTasks(roles)) {

            val userId = message.from!!.id
            val count = arguments.getOrNull(0)?.toInt() ?: 1
            var output = assignmentDao.findLastByAssigneeId(userId, count)
                .map { "${it.task} at ${it.createdAt.format()}" }
                .joinToString(separator = "\n")

            if (output.isEmpty()) output = "У тебя пока нет задач."

            responder.respond(text = "Список твоих задач (max=${count}):\n${output}")
        }
    }

    private fun canGetTasks(roles: Set<String>): Boolean = isAdmin(roles) || isUser(roles)

    private fun Date.format(): String =
        toInstant()
        .atZone(ZoneId.of(MOSCOW_TZ))
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

}

const val MOSCOW_TZ = "Europe/Moscow"