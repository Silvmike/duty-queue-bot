package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.User

class AssignTaskNotificationListener(
    private val bot: Bot,
    private val groupNotificationDao: GroupNotificationDao
): AssignTaskCommand.Listener {

    override fun onSuccess(responder: Responder, dutyQueue: DutyQueue, assignment: Assignment, assignee: User) {

        groupNotificationDao.findByOwnerId(dutyQueue.ownerId).forEach {

            bot.sendMessage(
                ChatId.fromId(it.chatId),
                text = """Пользователь @${assignee.username} получает новую задачу по колесу ${assignment.task}.
                    |Поздравляем!""".trimMargin(),
                parseMode = ParseMode.MARKDOWN
            )
        }
    }
}