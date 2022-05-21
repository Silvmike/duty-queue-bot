package ru.silvmike.bot.service

/**
 * Generates tokens which will be used for user registration.
 */
interface TokenGenerator {

    /**
     * Generates unique token.
     *
     * @return generated token
     */
    fun next(): String

}