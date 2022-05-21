package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.Token
import ru.silvmike.bot.service.TokenGenerator
import java.util.*

class InviteCommand(
    private val tokenDao: TokenDao,
    private val tokenGenerator: TokenGenerator,
    authService: AuthService
): AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {

        if (canInvite(roles)) {

            var role = arguments.getOrNull(0) ?: AuthService.ADMIN

            if (!isTheBoss(roles)) {
                role = AuthService.USER
            }

            val creatorId: Long = message.from!!.id
            val nextToken = tokenGenerator.next()

            tokenDao.save(Token(token = nextToken, createdAt = Date(), role = role, creatorId = creatorId))
            responder.respond(text = "/register ${nextToken}")
        }
    }

    private fun canInvite(roles: Set<String>): Boolean {
        return isAdmin(roles) || isTheBoss(roles)
    }

}