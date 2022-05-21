package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.ShadowService
import ru.silvmike.bot.command.api.Responder

class ShadowCommand(
    private val shadowService: ShadowService,
    private val authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        val userId = message.from!!.id
        val rolesDirect = shadowService.getRolesDirectly(userId)
        if (isTheBoss(rolesDirect)) {
            shadowService.shadow(userId, arguments.toSet())
            responder.respond(text = "Your set of roles now is ${authService.getRoles(userId)}.")
        } else {
            responder.respond(text = "Only the Boss can do it.")
        }
    }
}