package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

private const val TEST_USER_ID = 1L
private const val TEST_USER_NAME = "test"

class PromoteCommandTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)
    private val responder: Responder = mockk(relaxed = true)

    private val command = PromoteCommand(userDao, authService)

    @BeforeEach
    fun setUp() {
        clearMocks(userDao, authService, responder)
        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.SUPER)
    }

    @Test
    fun promote() {

        val username = TEST_USER_NAME

        val user = User(id = TEST_USER_ID, username = username, roles = mutableSetOf(AuthService.USER))
        every { userDao.findByUsername(username) } returns user

        command.executeCommand(responder, TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify(exactly = 1) { userDao.findByUsername(username) }
        verify(exactly = 1) { userDao.save(user) }
        verify(exactly = 1) { responder.respond(text = "${username} was assigned role '${AuthService.ADMIN}'") }

        Assertions.assertThat(user.roles).containsExactlyInAnyOrder(AuthService.USER, AuthService.ADMIN)
    }

    @Test
    fun onlyBossCanPromote() {

        val username = TEST_USER_NAME

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.USER)

        val user = User(id = TEST_USER_ID, username = username, roles = mutableSetOf(AuthService.USER))
        every { userDao.findByUsername(username) } returns user

        command.executeCommand(responder, TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify { userDao wasNot Called }
        verify { responder wasNot Called }

        Assertions.assertThat(user.roles).containsExactlyInAnyOrder(AuthService.USER)
    }

    @Test
    fun outputIsDifferentWhenUserNotFound() {

        val username = TEST_USER_NAME

        every { userDao.findByUsername(username) } returns null

        command.executeCommand(responder, TEST_USER_ID, listOf(username, AuthService.ADMIN))

        verify(exactly = 1) { userDao.findByUsername(username) }
        verify(exactly = 1) { responder.respond(text = "User '${username}' wasn't found") }
    }

    @Test
    fun commandRequiresExactlyTwoArguments() {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.SUPER)

        command.executeCommand(responder, TEST_USER_ID, listOf(TEST_USER_NAME))

        verify { userDao wasNot Called }
        verify(exactly = 1) { responder.respond(text = "The syntax is /promote [@]<username> <role>") }
    }

}