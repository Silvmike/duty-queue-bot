package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.command.api.Responder

class BecomeCommandTest {

    private val becomeService: BecomeService = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)
    private val responder: Responder = mockk(relaxed = true)

    private val command = BecomeCommand(becomeService, authService)

    @BeforeEach
    fun setUp() {
        clearMocks(becomeService, authService, responder)
    }

    @Test
    fun testSuperUserCanBecome() {

        val userId = 1L
        val otherUserId = 2L
        val otherUsername = "other"

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.SUPER)
        every { becomeService.whoAmI(userId) } returns otherUserId

        every { authService.getRoles(userId) } returns setOf(AuthService.ADMIN)

        command.executeCommand(responder, userId, listOf("@$otherUsername"))

        verify(exactly = 1) { becomeService.become(userId, otherUsername) }

        val textSlot = slot<String>()
        verify { responder.respond(text = capture(textSlot)) }

        Assertions.assertThat(textSlot.captured)
            .isEqualTo("Your userId [1] became [2], set of roles now is [admin].")

    }

    @Test
    fun testBecomeRequiresTargetUser() {

        val userId = 1L

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.SUPER)

        command.executeCommand(responder, userId, listOf())
        verify(exactly = 0) { becomeService.become(any(), any()) }
        verify { responder wasNot Called }
    }

    @Test
    fun onlyBossCanDoIt() {

        val userId = 1L
        val otherUsername = "other"

        every { becomeService.getRolesDirectly(userId) } returns setOf(AuthService.ADMIN)

        command.executeCommand(responder, userId, listOf("@$otherUsername"))

        verify(exactly = 0) { becomeService.become(any(), any()) }

        val textSlot = slot<String>()
        verify { responder.respond(text = capture(textSlot)) }

        Assertions.assertThat(textSlot.captured).isEqualTo("Only the Boss can do it.")
    }

}