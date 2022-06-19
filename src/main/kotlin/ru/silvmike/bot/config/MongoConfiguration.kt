package ru.silvmike.bot.config

import com.mongodb.client.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import ru.silvmike.bot.config.properties.EnvProperties
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.dao.mongo.AssignmentDaoImpl
import ru.silvmike.bot.dao.mongo.QueueDaoImpl
import ru.silvmike.bot.dao.mongo.TokenDaoImpl
import ru.silvmike.bot.dao.mongo.UserDaoImpl

@Configuration
@Import(MongoClientConfiguration::class)
@Profile(MONGO)
open class MongoConfiguration {

    @Bean
    open fun userDao(mongoClient: MongoClient, envProperties: EnvProperties): UserDao {
        return UserDaoImpl(mongoClient, envProperties.databaseName())
    }

    @Bean
    open fun tokenDao(mongoClient: MongoClient, envProperties: EnvProperties): TokenDao {
        return TokenDaoImpl(mongoClient, envProperties.databaseName())
    }

    @Bean
    open fun assignmentDao(mongoClient: MongoClient, envProperties: EnvProperties): AssignmentDao {
        return AssignmentDaoImpl(mongoClient, envProperties.databaseName())
    }

    @Bean
    open fun queueDao(mongoClient: MongoClient, envProperties: EnvProperties): QueueDao {
        return QueueDaoImpl(mongoClient, envProperties.databaseName())
    }

}