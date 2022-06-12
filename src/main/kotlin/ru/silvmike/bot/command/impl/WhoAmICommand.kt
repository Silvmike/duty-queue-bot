package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.command.api.Responder

class WhoAmICommand(
    private val becomeService: BecomeService,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        val userId = message.from!!.id
        responder.respond(text = "Твой userId = ${becomeService.whoAmI(userId)}.")
    }
}