package ru.silvmike.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.GroupNotificationDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.dao.stub.AssignmentDaoImpl
import ru.silvmike.bot.dao.stub.GroupNotificationDaoImpl
import ru.silvmike.bot.dao.stub.QueueDaoImpl
import ru.silvmike.bot.dao.stub.TokenDaoImpl
import ru.silvmike.bot.dao.stub.UserDaoImpl

@Configuration
@Profile(STUB)
open class StubConfiguration {

    @Bean
    open fun userDao(): UserDao = UserDaoImpl()

    @Bean
    open fun tokenDao(): TokenDao = TokenDaoImpl()

    @Bean
    open fun queueDao(): QueueDao = QueueDaoImpl()

    @Bean
    open fun assignmentDao(): AssignmentDao = AssignmentDaoImpl()

    @Bean
    open fun groupNotificationDao(): GroupNotificationDao = GroupNotificationDaoImpl()

}