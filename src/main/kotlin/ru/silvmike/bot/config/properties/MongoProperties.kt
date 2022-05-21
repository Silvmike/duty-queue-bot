package ru.silvmike.bot.config.properties

interface MongoProperties {

    fun connectionString(): String

    fun databaseName(): String

}