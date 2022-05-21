package ru.silvmike.bot.config.properties

class EnvProperties : BotProperties, MongoProperties {

    override fun token(): String =
        System.getenv().getOrDefault(BOT_TOKEN, "")

    override fun superUserId(): Long =
        System.getenv().getOrDefault(BOT_SUPER_USER_ID, "0").toLong()

    override fun connectionString(): String =
        System.getenv().getOrDefault(BOT_MONGO_CONNECTION_STRING, "")

    override fun databaseName(): String =
        System.getenv().getOrDefault(BOT_MONGO_DB_NAME, "")

    companion object {
        const val BOT_TOKEN = "BOT_TOKEN"
        const val BOT_SUPER_USER_ID = "BOT_SUPER_USER_ID"
        const val BOT_MONGO_CONNECTION_STRING = "BOT_MONGO_CONNECTION_STRING"
        const val BOT_MONGO_DB_NAME = "BOT_MONGO_DB_NAME"
    }

}