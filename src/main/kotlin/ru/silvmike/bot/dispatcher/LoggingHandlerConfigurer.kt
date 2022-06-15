package ru.silvmike.bot.dispatcher

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.handlers.Handler
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Update
import org.slf4j.LoggerFactory
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.ResponderImpl
import java.io.StringWriter

class LoggingHandlerConfigurer : DispatcherConfigurer {

    override fun configure(dispatcher: Dispatcher) {
        dispatcher.apply {
            addHandler(
                object : Handler {

                    val logger = LoggerFactory.getLogger("MESSAGING")

                    override fun checkUpdate(update: Update): Boolean = true

                    override fun handleUpdate(bot: Bot, update: Update) {

                        val messageJson =
                            StringWriter().apply {
                                jsonMapper().writerWithDefaultPrettyPrinter().writeValue(this, update.message)
                            }.toString()

                        logger.info("Received message: $messageJson")
                    }

                }
            )
        }
    }
}