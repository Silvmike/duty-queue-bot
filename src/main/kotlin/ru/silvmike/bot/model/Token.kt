package ru.silvmike.bot.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Represents token.
 *
 * @param id unique identifier
 * @param creatorId creator's user id
 * @param token the token
 * @param createdAt creation timestamp
 * @param role role this token enables
 * @param usedAt time when this token was used
 * @param usedBy used id of a user who had used this token
 */
data class Token(
    val creatorId: Long?,
    @JsonProperty("_id")
    val token: String,
    val createdAt: Date,
    val role: String,
    var usedAt: Date? = null,
    var usedBy: Long? = null
)