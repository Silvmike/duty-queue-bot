package ru.silvmike.bot.config.postprocessing

import com.github.kotlintelegrambot.Bot
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

interface BotAware : ApplicationContextAware {

    fun setBot(bot: Bot)

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        setBot(applicationContext.getBean(Bot::class.java))
    }

}