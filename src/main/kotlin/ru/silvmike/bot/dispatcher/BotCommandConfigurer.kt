package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.ResponderImpl

class BotCommandConfigurer(
    private val name: String,
    private val cmd: Command
) : DispatcherConfigurer {

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
}