package ru.silvmike.bot.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(value = [
    StubConfiguration::class,
    MongoConfiguration::class,
    ServiceConfiguration::class,
    CommandConfiguration::class,
    CommonConfiguration::class
])
open class RootConfiguration