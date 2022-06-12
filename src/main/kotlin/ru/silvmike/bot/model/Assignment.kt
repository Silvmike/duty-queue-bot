package ru.silvmike.bot.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Represents assignment.
 *
 * @param assigneeId user id of assignee
 * @param task task to assign
 */
data class Assignment(
    @JsonProperty("_id")
    val id: String = UUID.randomUUID().toString(),
    val ownerId: Long,
    val assigneeId: Long,
    val task: String,
    val createdAt: Date = Date()
) {

}