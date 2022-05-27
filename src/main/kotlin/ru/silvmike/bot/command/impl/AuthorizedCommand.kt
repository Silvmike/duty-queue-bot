package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import org.slf4j.LoggerFactory
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder

abstract class AuthorizedCommand(private val authService: AuthService) : Command {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(responder: Responder, message: Message, arguments: List<String>) {
        val roles = authService.getRoles(message.from!!.id)
        try {
            execute(responder, message, arguments, roles)
        } catch (e: Exception) {

            logger.error("${javaClass.simpleName} failed to execute", e)
            if (isTheBoss(roles)) {
                responder.respond(text = "Exception occurred. Stacktrace: ${e.stackTraceToString()}")
            }
        }
    }

    abstract fun execute(responder: Responder, message: Message, arguments: List<String> = listOf(), roles: Set<String>);

    protected fun isUser(roles: Set<String>) = AuthService.USER in roles

    protected fun isAdmin(roles: Set<String>) = AuthService.ADMIN in roles

    protected fun isTheBoss(roles: Set<String>) = AuthService.SUPER in roles

    protected fun isAnonymous(roles: Set<String>) = AuthService.ANONYMOUS in roles
}