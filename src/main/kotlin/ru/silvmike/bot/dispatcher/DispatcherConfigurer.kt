package ru.silvmike.bot.dispatcher

import com.github.kotlintelegrambot.dispatcher.Dispatcher

interface DispatcherConfigurer {

    fun configure(dispatcher: Dispatcher)

}