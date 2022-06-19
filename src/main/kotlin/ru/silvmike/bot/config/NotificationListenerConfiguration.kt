package ru.silvmike.bot.config

import com.github.kotlintelegrambot.Bot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.silvmike.bot.command.impl.AssignTaskCommand
import ru.silvmike.bot.command.impl.AssignTaskNotificationListener
import ru.silvmike.bot.dao.api.GroupNotificationDao

@Configuration
open class NotificationListenerConfiguration {

    @Bean
    open fun assignTaskNotificationListener(
        bot: Bot, groupNotificationDao: GroupNotificationDao): AssignTaskCommand.Listener =

        AssignTaskNotificationListener(bot, groupNotificationDao)

}