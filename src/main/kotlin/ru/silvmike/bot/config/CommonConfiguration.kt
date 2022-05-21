package ru.silvmike.bot.config

import com.github.kotlintelegrambot.bot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.silvmike.bot.config.properties.EnvProperties
import ru.silvmike.bot.dispatcher.BotConfigurer
import ru.silvmike.bot.dispatcher.DispatcherConfigurer
import ru.silvmike.bot.dispatcher.SimpleBotConfigurer

@Configuration
open class CommonConfiguration {

    @Bean
    open fun envProperties() = EnvProperties()

    @Bean
    open fun simpleBotConfigurer(
        dispatcherConfigurers: List<DispatcherConfigurer>,
        envProperties: EnvProperties
    ): BotConfigurer = SimpleBotConfigurer(
        envProperties.token(),
        dispatcherConfigurers
    )

    @Bean(initMethod = "startPolling", destroyMethod = "stopPolling")
    open fun bot(configurers: List<BotConfigurer>) = bot {
        configurers.forEach { it.configure(this) }
    }
}