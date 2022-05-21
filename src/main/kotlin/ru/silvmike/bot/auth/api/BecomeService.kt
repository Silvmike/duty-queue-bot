package ru.silvmike.bot.auth.api

interface BecomeService {

    fun getRolesDirectly(userId: Long): Set<String>

    fun become(userId: Long, targetUsername: String)

    fun whoAmI(userId: Long): Long

}