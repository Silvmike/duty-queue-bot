package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.Bot

interface BotConfigurer {

    fun configure(builder: Bot.Builder)
}