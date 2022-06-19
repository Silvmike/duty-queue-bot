package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.ResponderImpl
import ru.silvmike.bot.config.postprocessing.BotAware

class BotCommandConfigurer(
    private val name: String,
    private val cmd: Command
) : DispatcherConfigurer, BotAware {

    override fun configure(dispatcher: Dispatcher) {
        dispatcher.apply {
            command(name) {
                cmd.execute(
                    ResponderImpl(bot, message),
                    message,
                    args
                )
            }
        }
    }

    override fun setBot(bot: Bot) {
        if (cmd is BotAware) cmd.setBot(bot)
    }
}