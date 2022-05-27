package ru.silvmike.bot.command.impl

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.ShadowService

class ShadowCommandTest: AbstractCommandTest() {

    private val shadowService: ShadowService = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = ShadowCommand(shadowService, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(shadowService, authService)
    }

    @Test
    fun testSuperUserCanShadow() {

        val userId = 1L

        every { shadowService.getRolesDirectly(userId) } returns setOf(AuthService.SUPER)

        command.executeCommand(userId, listOf(AuthService.ADMIN))

        verify(exactly = 1) { shadowService.shadow(userId, setOf(AuthService.ADMIN)) }
        verifyMessage("Your set of roles now is ${authService.getRoles(userId)}.")
    }

    @Test
    fun onlyBossCanDoIt() {

        val userId = 1L

        every { shadowService.getRolesDirectly(userId) } returns setOf(AuthService.USER)

        command.executeCommand(userId, listOf(AuthService.ADMIN))

        verify(exactly = 0) { shadowService.shadow(any(), any()) }
        verifyMessage("Only the Boss can do it.")
    }

}