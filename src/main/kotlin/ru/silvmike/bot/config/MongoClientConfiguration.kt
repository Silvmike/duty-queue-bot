package ru.silvmike.bot.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.silvmike.bot.config.properties.EnvProperties

@Configuration
open class MongoClientConfiguration {

    @Bean(destroyMethod = "close")
    open fun mongoClient(envProperties: EnvProperties): MongoClient {

        return KMongo.createClient(
            MongoClientSettings.builder()
                .applyConnectionString(
                    ConnectionString(
                        envProperties.connectionString()
                    )
                )
                .serverApi(
                    ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build())
                .build()
        )
    }

}