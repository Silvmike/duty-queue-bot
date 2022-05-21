package ru.silvmike.bot.command.api

import com.github.kotlintelegrambot.entities.Message

interface Command {

    fun execute(responder: Responder, message: Message, arguments: List<String> = listOf())

}