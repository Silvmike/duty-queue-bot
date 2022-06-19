package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatch
import ru.silvmike.bot.config.postprocessing.BotAware

class SimpleBotConfigurer(
    private val botToken: String,
    private val dispatcherConfigurers: List<DispatcherConfigurer>
) : BotConfigurer, BotAware {

    override fun configure(builder: Bot.Builder) {

        builder.apply {
            token = botToken
            dispatch {
                dispatcherConfigurers.forEach { it.configure(this) }
            }
        }
    }

    override fun setBot(bot: Bot) {
        dispatcherConfigurers.forEach {
            if (it is BotAware) it.setBot(bot)
        }
    }

}