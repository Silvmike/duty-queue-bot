package ru.silvmike.bot.auth.impl

import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.auth.api.ShadowService
import ru.silvmike.bot.dao.api.UserDao
import java.util.*

class ShadowBecomeAuthService(
    private val userDao: UserDao,
    private val delegate: AuthService
) : AuthService, ShadowService, BecomeService {

    private val overrides = Collections.synchronizedMap(HashMap<Long, Set<String>?>())
    private val userIdOverrides = Collections.synchronizedMap(HashMap<Long, Long>())

    override fun getRoles(userId: Long): Set<String> {
        val targetUserId = whoAmI(userId)
        return overrides.getOrElse(targetUserId) { delegate.getRoles(targetUserId) }!!
    }

    override fun assignRole(userId: Long, role: String) {
        val targetUserId = whoAmI(userId)
        if (targetUserId != userId) return

        val current = overrides[targetUserId]
        if (current != null && current.isNotEmpty()) {
            overrides[targetUserId] = current.plus(role)
        } else {
            delegate.assignRole(targetUserId, role)
        }
    }

    override fun getRolesDirectly(userId: Long): Set<String> {
        return delegate.getRoles(userId)
    }

    override fun shadow(userId: Long, roles: Set<String>) {
        overrides[userId] = roles.ifEmpty { null }
    }

    override fun become(userId: Long, targetUsername: String) {

        if (targetUsername.isEmpty()) {
            userIdOverrides[userId] = null
        } else {
            userDao.findByUsername(targetUsername)?.apply {
                userIdOverrides[userId] = id
            }
        }
    }

    override fun whoAmI(userId: Long): Long = userIdOverrides.getOrDefault(userId, userId)

}