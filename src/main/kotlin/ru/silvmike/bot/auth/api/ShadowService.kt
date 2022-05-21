package ru.silvmike.bot.auth.api

interface ShadowService {

    fun getRolesDirectly(userId: Long): Set<String>

    fun shadow(userId: Long, roles: Set<String>)

}