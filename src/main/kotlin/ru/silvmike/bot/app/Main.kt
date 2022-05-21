package ru.silvmike.bot.app

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.silvmike.bot.config.MONGO
import ru.silvmike.bot.config.RootConfiguration

fun main(args: Array<String>) {

    val context = AnnotationConfigApplicationContext()
    context.environment.setActiveProfiles(MONGO);
    context.register(RootConfiguration::class.java)
    context.refresh()
    context.registerShutdownHook()

}