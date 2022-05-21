package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Chat
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder

internal fun Command.executeCommand(
    responder: Responder, userId: Long, arguments: List<String> = listOf()) {

    execute(
        responder,
        Message(
            messageId = 1L,
            date = 2L,
            chat = Chat(id = 2, type = "user"),
            from = User(id = userId, isBot = false, firstName = "John")
        ),
        arguments
    )
}