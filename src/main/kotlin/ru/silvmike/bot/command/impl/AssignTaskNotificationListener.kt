package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.command.escaped
import ru.silvmike.bot.config.postprocessing.BotAware
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.User

class AssignTaskNotificationListener(
    private val groupNotificationDao: GroupNotificationDao
): AssignTaskCommand.Listener, BotAware {

    private lateinit var botReference: Bot

    override fun onSuccess(responder: Responder, dutyQueue: DutyQueue, assignment: Assignment, assignee: User) {

        groupNotificationDao.findByOwnerId(dutyQueue.ownerId).forEach {

            botReference.sendMessage(
                ChatId.fromId(it.chatId),
                text = """Пользователь @${assignee.username.escaped()} получает новую задачу 
                    по колесу ${assignment.task.escaped()}.
                    |Поздравляем!""".trimMargin(),
                parseMode = ParseMode.MARKDOWN,
                allowSendingWithoutReply = true
            )
        }
    }

    override fun setBot(bot: Bot) {
        this.botReference = bot
    }
}