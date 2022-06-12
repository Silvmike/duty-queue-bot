package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.UserDao

class PromoteCommand(
    private val userDao: UserDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        if (!isTheBoss(roles)) return

        if (arguments.size == 2) {

            val username = arguments[0].removePrefix("@")
            val user = userDao.findByUsername(username)
            if (user != null) {
                val role = arguments[1]
                user.roles.add(role)
                userDao.save(user)
                responder.respond(text = "Пользователю ${username} была назначена роль '${role}'")
            } else {
                responder.respond(text = "Пользователь '${username}' не найден")
            }
        } else {
            responder.respond(text = "Синтаксис /promote [@]<username> <role>")
        }
    }
}