package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.command.isGroup
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.model.GroupNotification

private val ALLOWED_ACTIONS = setOf("add", "remove")
private val ALREADY_EXIST_MESSAGE = "Указанная группа уже получает уведомления"
private val NOTIFICATION_DISABLED_MESSAGE = "Уведомления для данного чата отключены"

class NotifyTaskCommand(
    private val groupNotificationDao: GroupNotificationDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(
        responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        if (isAdmin(roles)) {

            if (isArgumentsInvalid(arguments)) {
                responder.respond("Синтаксис: /notify (add|remove)")
                return
            }

            if (!message.chat.isGroup()) {
                responder.respond("/notify предназначена для указания группы для уведомлений")
            }

            val action = arguments[0]

            val existingNotification = message.from?.let {
                groupNotificationDao.findByOwnerAndChatId(
                    it.id,
                    message.chat.id
                )
            }

            when (action) {
                "remove" -> disableNotifications(existingNotification, responder)
                "add" -> enableNotifications(existingNotification, message, responder)
            }

        }
    }

    private fun enableNotifications(existingNotification: GroupNotification?, message: Message, responder: Responder) {
        if (existingNotification == null) {
            val notification = GroupNotification(
                ownerId = message.from!!.id,
                chatId = message.chat.id,
                chatType = message.chat.type
            )
            groupNotificationDao.save(notification)
            responder.respond("Уведомления данной группы включены")
        } else {
            responder.respond(ALREADY_EXIST_MESSAGE)
        }
    }

    private fun disableNotifications(existingNotification: GroupNotification?, responder: Responder) {
        if (existingNotification != null) {
            groupNotificationDao.remove(existingNotification.id)
        }
        responder.respond(NOTIFICATION_DISABLED_MESSAGE)
    }

    private fun isArgumentsInvalid(arguments: List<String>) =
        arguments.isEmpty() || !ALLOWED_ACTIONS.contains(arguments[0])
}