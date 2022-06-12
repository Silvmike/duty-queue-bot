package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.model.Token
import ru.silvmike.bot.service.TokenGenerator
import java.util.stream.Stream

private const val TEST_USER_ID = 1L
private const val TEST_TOKEN = "TOKEN"

class InviteCommandTest: AbstractCommandTest() {

    private val tokenDao: TokenDao = mockk(relaxed = true)
    private val tokenGenerator: TokenGenerator = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = InviteCommand(tokenDao, tokenGenerator, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(tokenDao, tokenGenerator, authService)
    }

    private fun testData(): Stream<Arguments> =
        Stream.of(
            arguments(AuthService.SUPER, null, AuthService.ADMIN),
            arguments(AuthService.SUPER, AuthService.ADMIN, AuthService.ADMIN),
            arguments(AuthService.SUPER, AuthService.USER, AuthService.USER),
            arguments(AuthService.ADMIN, null, AuthService.USER),
            arguments(AuthService.ADMIN, AuthService.ADMIN, AuthService.USER),
            arguments(AuthService.ADMIN, AuthService.USER, AuthService.USER)
        )

    @ParameterizedTest(name = "When {0} requests a token with role {1} they get a token with {2}")
    @MethodSource("testData")
    fun test(callerRole: String, requestedTokenRoles: String?, expectedTokenRoles: String) {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(callerRole)
        every { tokenGenerator.next() } returns TEST_TOKEN

        command.executeCommand(TEST_USER_ID, requestedTokenRoles?.let { listOf(it) } ?: listOf())

        val tokenSlot = slot<Token>()
        verify { tokenDao.save(capture(tokenSlot)) }

        Assertions.assertThat(tokenSlot.captured.role).isEqualTo(expectedTokenRoles)
        verifyMessage(text = "/register ${TEST_TOKEN}")
    }

    @Test
    fun regularUserCannotDoIt() {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.USER)

        command.executeCommand(TEST_USER_ID, listOf(AuthService.USER))
        verify { tokenDao wasNot Called }
    }

}