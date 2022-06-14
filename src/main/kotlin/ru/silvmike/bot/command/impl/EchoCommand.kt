package ru.silvmike.bot.command.impl

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder
import java.io.StringWriter

class EchoCommand : Command {

    override fun execute(responder: Responder, message: Message, arguments: List<String>) {
        val messageJson =
            StringWriter().apply {
                jsonMapper().writerWithDefaultPrettyPrinter().writeValue(this, message)
            }.toString()

        responder.respond("Received message:\n\n$messageJson")
    }
}