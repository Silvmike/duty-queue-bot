package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

private const val TEST_USER_ID = 1L
private const val TEST_USER_NAME = "test"

class PromoteCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = PromoteCommand(userDao, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(userDao, authService)
        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.SUPER)
    }

    @Test
    fun promote() {

        val username = TEST_USER_NAME

        val user = User(id = TEST_USER_ID, username = username, roles = mutableSetOf(AuthService.USER))
        every { userDao.findByUsername(username) } returns user

        command.executeCommand(TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify(exactly = 1) { userDao.findByUsername(username) }
        verify(exactly = 1) { userDao.save(user) }
        verifyMessage(text = "Пользователю ${username} была назначена роль '${AuthService.ADMIN}'")

        Assertions.assertThat(user.roles).containsExactlyInAnyOrder(AuthService.USER, AuthService.ADMIN)
    }

    @Test
    fun onlyBossCanPromote() {

        val username = TEST_USER_NAME

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.USER)

        val user = User(id = TEST_USER_ID, username = username, roles = mutableSetOf(AuthService.USER))
        every { userDao.findByUsername(username) } returns user

        command.executeCommand(TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify { userDao wasNot Called }
        verifyNoMessage()

        Assertions.assertThat(user.roles).containsExactlyInAnyOrder(AuthService.USER)
    }

    @Test
    fun outputIsDifferentWhenUserNotFound() {

        val username = TEST_USER_NAME

        every { userDao.findByUsername(username) } returns null

        command.executeCommand(TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify(exactly = 1) { userDao.findByUsername(username) }
        verifyMessage(text = "Пользователь '${username}' не найден")
    }

    @Test
    fun commandRequiresExactlyTwoArguments() {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.SUPER)

        command.executeCommand(TEST_USER_ID, listOf(TEST_USER_NAME))

        verify { userDao wasNot Called }
        verifyMessage(text = "Синтаксис /promote [@]<username> <role>")
    }

}