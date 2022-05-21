package ru.silvmike.bot.auth.impl

import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.UserDao

class SimpleAuthService(private val superUserId: Long, private val userDao: UserDao) : AuthService {

    override fun getRoles(userId: Long) =
        if (superUserId == userId) {
            setOf(AuthService.SUPER)
        } else {
            userDao.findById(userId)?.roles ?: setOf(AuthService.ANONYMOUS)
        }

    override fun assignRole(userId: Long, role: String) {
        userDao.findById(userId)?.apply {
            roles.add(role)
            userDao.save(this)
        }
    }

}