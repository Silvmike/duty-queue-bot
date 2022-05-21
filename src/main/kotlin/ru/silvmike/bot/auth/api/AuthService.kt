package ru.silvmike.bot.auth.api

interface AuthService {

    fun getRoles(userId: Long): Set<String>

    fun assignRole(userId: Long, role: String)

    companion object Roles {
        const val SUPER = "super"
        const val USER = "user"
        const val ADMIN = "admin"
        const val ANONYMOUS = "anonymous"
    }

}