package ru.silvmike.bot.command.impl

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService

private const val TEST_USER_ID = 1L
private const val BECOME_USER_ID = 2L

class WhoAmICommandTest: AbstractCommandTest() {

    private val authService: AuthService = mockk(relaxed = true)
    private val becomeService: BecomeService = mockk(relaxed = true)

    private val command = WhoAmICommand(becomeService, authService)

    @Test
    fun test() {

        every { becomeService.whoAmI(TEST_USER_ID) } returns BECOME_USER_ID

        command.executeCommand(TEST_USER_ID, listOf())
        verifyMessage(text = "Твой userId = ${BECOME_USER_ID}.")
    }

}