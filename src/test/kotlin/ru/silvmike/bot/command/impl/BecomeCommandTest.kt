package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.command.api.Responder

class BecomeCommandTest: AbstractCommandTest() {

    private val becomeService: BecomeService = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = BecomeCommand(becomeService, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(becomeService, authService)
    }

    @Test
    fun testSuperUserCanBecome() {

        val userId = 1L
        val otherUserId = 2L
        val otherUsername = "other"

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.SUPER)
        every { becomeService.whoAmI(userId) } returns otherUserId

        every { authService.getRoles(userId) } returns setOf(AuthService.ADMIN)

        command.executeCommand(userId, listOf("@$otherUsername"))

        verifyMessage(text = "Your userId [1] became [2], set of roles now is [admin].")

    }

    @Test
    fun testBecomeRequiresTargetUser() {

        val userId = 1L

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.SUPER)

        command.executeCommand(userId, listOf())

        verify(exactly = 0) { becomeService.become(any(), any()) }
        verifyNoMessage()
    }

    @Test
    fun onlyBossCanDoIt() {

        val userId = 1L
        val otherUsername = "other"

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.ADMIN)

        command.executeCommand(userId, listOf("@$otherUsername"))

        verify(exactly = 0) { becomeService.become(any(), any()) }
        verifyMessage("Only the Boss can do it.")
    }

}