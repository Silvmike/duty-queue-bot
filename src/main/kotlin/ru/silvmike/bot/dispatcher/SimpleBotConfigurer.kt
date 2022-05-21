package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatch

class SimpleBotConfigurer(
    private val botToken: String,
    private val dispatcherConfigurers: List<DispatcherConfigurer>
) : BotConfigurer {

    override fun configure(builder: Bot.Builder) {

        builder.apply {
            token = botToken
            dispatch {
                dispatcherConfigurers.forEach { it.configure(this) }
            }
        }
    }


}