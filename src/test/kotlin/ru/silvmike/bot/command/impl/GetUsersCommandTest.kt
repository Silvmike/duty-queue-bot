package ru.silvmike.bot.command.impl

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

private const val TEST_USER_ID = 1L

class GetUsersCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = GetUsersCommand(userDao, authService)

    @ParameterizedTest
    @ValueSource(strings = [AuthService.ADMIN, AuthService.USER, AuthService.ANONYMOUS, "other"])
    fun onlyBossCanDoIt(role: String) {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(role)

        command.executeCommand(TEST_USER_ID, listOf())
        verifyNoMessage()
    }

    @Test
    fun returnsWhateverUserDaoReturns() {

        val user1 = User(
            id = 2L,
            username = "user1",
            roles = mutableSetOf(AuthService.ADMIN, "other")
        )

        val user2 = User(
            id = 3L,
            username = "user2",
            roles = mutableSetOf(AuthService.USER)
        )

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.SUPER)
        every { userDao.findAll() } returns listOf(user1, user2).asSequence()

        command.executeCommand(TEST_USER_ID, listOf())
        verifyMessage("user1/admin,other/2\n"
            + "user2/user/3")
    }
}