package ru.silvmike.bot.command.impl

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.command.api.Responder

private const val TEST_USER_ID = 1L
private const val BECOME_USER_ID = 2L

class WhoAmICommandTest {

    private val authService: AuthService = mockk(relaxed = true)
    private val becomeService: BecomeService = mockk(relaxed = true)
    private val responder: Responder = mockk(relaxed = true)

    private val command = WhoAmICommand(becomeService, authService)

    @Test
    fun test() {

        every { becomeService.whoAmI(TEST_USER_ID) } returns BECOME_USER_ID

        command.executeCommand(responder, TEST_USER_ID, listOf())

        verify(exactly = 1) { responder.respond(text = "Your userId is ${BECOME_USER_ID}.") }
    }

}