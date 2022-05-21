package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.UserDao

class GetUsersCommand(
    private val userDao: UserDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        if (isTheBoss(roles)) {

            responder.respond(
                text = userDao.findAll()
                    .map { "${it.username}/${it.roles.joinToString(separator = ",")}/${it.id}" }
                    .joinToString(separator = "\n")
            )
        }
    }
}