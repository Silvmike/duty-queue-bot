package ru.silvmike.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.silvmike.bot.auth.impl.ShadowBecomeAuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.config.properties.EnvProperties
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.service.BasicTokenGenerator
import ru.silvmike.bot.service.TokenGenerator

@Configuration
open class ServiceConfiguration {

    @Bean
    open fun tokenGenerator(): TokenGenerator = BasicTokenGenerator()

    @Bean
    open fun authService(userDao: UserDao, envProperties: EnvProperties): ShadowBecomeAuthService =
        ShadowBecomeAuthService(
            userDao,
            SimpleAuthService(superUserId = envProperties.superUserId(), userDao = userDao)
        ).apply {
            shadow(envProperties.superUserId(), setOf("admin"))
        }

}