package ru.silvmike.bot.service

import java.util.*

/**
 * Basic [TokenGenerator] implementation.
 */
class BasicTokenGenerator: TokenGenerator {

    override fun next(): String = "${UUID.randomUUID()}.${System.currentTimeMillis()}".toBase64()

}

internal fun String.toBase64() = String(Base64.getEncoder().encode(this.encodeToByteArray()))