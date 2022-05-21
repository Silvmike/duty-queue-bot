package ru.silvmike.bot.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents user entity.
 *
 * @param id user id
 * @param username user name
 * @param roles set of assigned roles
 */
data class User(
    @JsonProperty("_id")
    val id: Long,
    val username: String,
    var roles: MutableSet<String>
)
