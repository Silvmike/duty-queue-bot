package ru.silvmike.bot.command.api

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message

class ResponderImpl(
    private val bot: Bot,
    private val message: Message): Responder {

    override fun respond(text: String) {
        bot.sendMessage(ChatId.fromId(message.chat.id), text = text)
    }
}