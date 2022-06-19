package ru.silvmike.bot.command

import com.github.kotlintelegrambot.entities.Chat

fun Chat.isGroup() = type.indexOf("group") != -1

fun Chat.isSuperGroup() = "supergroup" == type
