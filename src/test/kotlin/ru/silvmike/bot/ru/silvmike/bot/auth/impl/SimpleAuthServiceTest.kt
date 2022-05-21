package ru.silvmike.bot.ru.silvmike.bot.auth.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

private const val TEST_SUPER_USER_ID = 100L;

class SimpleAuthServiceTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService = SimpleAuthService(TEST_SUPER_USER_ID, userDao)

    @Test
    fun testGetRolesWhenUserIsFound() {

        val userId = 2L
        val expectedRoleSet = mutableSetOf(AuthService.USER)

        every { userDao.findById(any()) } returns User(userId, "test", expectedRoleSet)

        Assertions.assertThat(authService.getRoles(userId)).containsExactlyInAnyOrderElementsOf(expectedRoleSet)
        verify(exactly = 1) { userDao.findById(userId) }
    }

    @Test
    fun testGetRolesWhenUserIsNotFound() {

        val userId = 2L

        every { userDao.findById(any()) } returns null

        Assertions.assertThat(authService.getRoles(userId)).containsExactly(AuthService.ANONYMOUS)
        verify(exactly = 1) { userDao.findById(userId) }
    }

    @Test
    fun testAssignRolesWhenUserIsFound() {

        val userId = 2L
        val initialRoleSet = mutableSetOf(AuthService.USER)
        val expectedRoleSet = mutableSetOf(AuthService.USER, AuthService.ADMIN)

        every { userDao.findById(any()) } returns User(userId, "test", initialRoleSet)


        Assertions.assertThat(authService.assignRole(userId, AuthService.ADMIN))
        verify(exactly = 1) { userDao.findById(userId) }
        verify(exactly = 1) { userDao.save(any()) }

        Assertions.assertThat(authService.getRoles(userId)).containsExactlyInAnyOrderElementsOf(expectedRoleSet)
    }

    @Test
    fun testAssignRolesWhenUserIsNotFound() {

        val userId = 2L

        every { userDao.findById(any()) } returns null

        Assertions.assertThat(authService.assignRole(userId, AuthService.ADMIN))
        verify(exactly = 1) { userDao.findById(userId) }
        verify(exactly = 0) { userDao.save(any()) }

        Assertions.assertThat(authService.getRoles(userId)).containsExactly(AuthService.ANONYMOUS)
    }

}