package ru.silvmike.bot.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents duty queue.
 *
 * @param ownerId user id for this queue admin
 * @param queue the queue
 */
data class DutyQueue(
    @JsonProperty("_id")
    val ownerId: Long,
    val queue: List<Long>
)