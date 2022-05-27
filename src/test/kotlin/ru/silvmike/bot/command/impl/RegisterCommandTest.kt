package ru.silvmike.bot.command.impl

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.Token
import java.util.*

private const val TEST_USER_ID = 1L

class RegisterCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val tokenDao: TokenDao = mockk(relaxed = true)
    private val queueDao: QueueDao = mockk(relaxed = true)

    private val command = RegisterCommand(tokenDao, userDao, queueDao)

    @Test
    fun nullToken() {

        command.executeCommand(TEST_USER_ID, listOf())
        verifyInvalidTokenMessage()
    }

    @Test
    fun absentToken() {

        every { tokenDao.get(any()) } returns null

        command.executeCommand(TEST_USER_ID, listOf("absent"))
        verifyInvalidTokenMessage()
    }

    @Test
    fun takenToken() {

        val invalidToken = Token(token = "1", usedBy = 123, createdAt = Date(), creatorId = 321, role = AuthService.ADMIN)
        every { tokenDao.get(any()) } returns invalidToken

        command.executeCommand(TEST_USER_ID, listOf(invalidToken.token))
        verifyInvalidTokenMessage()
    }

    private fun verifyInvalidTokenMessage() {
        verifyMessage(text = "Your token is invalid!")
    }

    @Test
    fun validToken() {

        val validToken = Token(token = "1", createdAt = Date(), creatorId = 321, role = AuthService.ADMIN)
        every { tokenDao.get(any()) } returns validToken

        Assertions.assertThat(validToken.usedBy).isNull()

        command.executeCommand(TEST_USER_ID, listOf(validToken.token))

        Assertions.assertThat(validToken.usedBy).isEqualTo(TEST_USER_ID)
        verifyMessage("OK!")
    }
}
