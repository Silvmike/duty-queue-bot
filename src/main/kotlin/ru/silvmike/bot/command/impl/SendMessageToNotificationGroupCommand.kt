package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import org.litote.kmongo.text
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.config.postprocessing.BotAware
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.GroupNotification

class SendMessageToNotificationGroupCommand(
    val notificationDao: GroupNotificationDao,
    val authService: AuthService
): AuthorizedCommand(authService), BotAware {

    private lateinit var botReference: Bot

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        val userId = message.from!!.id

        if (isAdmin(roles)) {

            if (arguments.isNotEmpty()) {

                notificationDao.findByOwnerId(userId).forEach {
                    botReference.sendMessage(
                        ChatId.fromId(it.chatId),
                        text = message.text!!.substring("/send".length).trim(),
                        parseMode = ParseMode.MARKDOWN,
                        allowSendingWithoutReply = true
                    )
                }
            } else {
                responder.respond("Синтаксис /send <message to send>.")
            }
        }
    }

    override fun setBot(bot: Bot) {
        this.botReference = bot
    }
}