package ru.silvmike.bot.config.postprocessing

import com.github.kotlintelegrambot.Bot

interface BotAware {

    fun setBot(bot: Bot)

}