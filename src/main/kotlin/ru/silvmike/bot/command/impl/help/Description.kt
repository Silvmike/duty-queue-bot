package ru.silvmike.bot.command.impl.help

data class Description(val command: String, val arguments: String = "", val description: String) {

    override fun toString(): String {
        return "$command $arguments\n$description"
    }
}