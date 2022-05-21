package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.command.api.Responder

class BecomeCommand(
    private val becomeService: BecomeService,
    private val authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        val userId = message.from!!.id
        val rolesDirect = becomeService.getRolesDirectly(userId)
        if (isTheBoss(rolesDirect)) {

            val destUsername = arguments.getOrNull(0)?.removePrefix("@") ?: return

            becomeService.become(userId, destUsername)
            responder.respond(text = "Your userId [${userId}] became [${becomeService.whoAmI(userId)}], set of roles now is ${authService.getRoles(userId)}.")
        } else {
            responder.respond(text = "Only the Boss can do it.")
        }
    }
}