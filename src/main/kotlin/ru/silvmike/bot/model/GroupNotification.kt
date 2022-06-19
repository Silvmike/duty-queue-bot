package ru.silvmike.bot.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class GroupNotification(
    @JsonProperty("_id")
    val id: String = UUID.randomUUID().toString(),
    val ownerId: Long,
    val chatId: Long,
    val chatType: String
)