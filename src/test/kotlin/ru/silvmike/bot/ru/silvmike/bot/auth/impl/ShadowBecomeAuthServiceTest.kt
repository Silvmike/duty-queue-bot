package ru.silvmike.bot.ru.silvmike.bot.auth.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.impl.ShadowBecomeAuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

private const val TEST_SUPER_USER_ID = 100L;

class ShadowBecomeAuthServiceTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService = spyk(SimpleAuthService(TEST_SUPER_USER_ID, userDao))

    @BeforeEach
    fun setUp() {
        clearMocks(userDao, authService)
    }

    @Test
    fun testDelegatesGetRolesToAuthServiceWhenNotConfigured() {

        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        val userId = 2L
        val expectedRoleSet = mutableSetOf(AuthService.USER)

        every { authService.getRoles(userId) } returns expectedRoleSet

        Assertions.assertThat(shadowAuthService.getRoles(userId)).containsExactlyInAnyOrderElementsOf(expectedRoleSet)
        verify(exactly = 1) { authService.getRoles(userId) }
    }

    @Test
    fun testDelegatesAssignRoleToAuthServiceWhenNotConfigured() {

        val userId = 2L
        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        shadowAuthService.assignRole(userId, AuthService.USER)
        verify(exactly = 1) { authService.assignRole(userId, AuthService.USER) }
    }

    @Test
    fun testShadowOverridesRoles() {

        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        val userId = 2L
        val otherUserId = 3L
        val authServiceRoleSet = mutableSetOf(AuthService.USER)
        val overriddenRoleSet = mutableSetOf(AuthService.ADMIN)

        every { authService.getRoles(any()) } returns authServiceRoleSet

        shadowAuthService.shadow(userId, overriddenRoleSet)

        Assertions.assertThat(shadowAuthService.getRoles(userId))
            .containsExactlyInAnyOrderElementsOf(overriddenRoleSet)
        verify(exactly = 0) { authService.getRoles(userId) }

        Assertions.assertThat(shadowAuthService.getRoles(otherUserId))
            .containsExactlyInAnyOrderElementsOf(authServiceRoleSet)
    }

    @Test
    fun testBecomeOverridesUserId() {

        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        val userId = 2L
        val otherUserId = 3L
        val otherUsername = "other"
        val authServiceRoleSet = mutableSetOf(AuthService.USER)
        val overriddenRoleSet = mutableSetOf(AuthService.ADMIN)

        every { authService.getRoles(userId) } returns authServiceRoleSet
        every { authService.getRoles(otherUserId) } returns overriddenRoleSet
        every { userDao.findByUsername(otherUsername) } returns User(otherUserId, otherUsername, overriddenRoleSet)

        shadowAuthService.become(userId, otherUsername)

        Assertions.assertThat(shadowAuthService.getRoles(userId))
            .containsExactlyInAnyOrderElementsOf(overriddenRoleSet)

        verify(exactly = 0) { authService.getRoles(userId) }
        verify(exactly = 1) { authService.getRoles(otherUserId) }
    }

    @Test
    fun testOverriddenUserIdProhibitsRoleAssignmentForThatUserId() {

        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        val userId = 2L
        val otherUserId = 3L
        val otherUsername = "other"
        val authServiceRoleSet = mutableSetOf(AuthService.USER)
        val overriddenRoleSet = mutableSetOf(AuthService.ADMIN)

        every { authService.getRoles(userId) } returns authServiceRoleSet
        every { authService.getRoles(otherUserId) } returns overriddenRoleSet
        every { userDao.findByUsername(otherUsername) } returns User(otherUserId, otherUsername, overriddenRoleSet)

        shadowAuthService.become(userId, otherUsername)

        shadowAuthService.assignRole(userId, AuthService.ADMIN)
        verify(exactly = 0) { authService.assignRole(userId, AuthService.ADMIN) }
        verify(exactly = 0) { authService.assignRole(otherUserId, AuthService.ADMIN) }
    }

    @Test
    fun testGetRolesDirectly() {

        val shadowAuthService = ShadowBecomeAuthService(userDao, authService)

        val userId = 2L
        val otherUserId = 3L
        val otherUsername = "other"
        val authServiceRoleSet = mutableSetOf(AuthService.USER)
        val overriddenRoleSet = mutableSetOf(AuthService.ADMIN)

        every { authService.getRoles(userId) } returns authServiceRoleSet
        every { authService.getRoles(otherUserId) } returns overriddenRoleSet
        every { userDao.findByUsername(otherUsername) } returns User(otherUserId, otherUsername, overriddenRoleSet)

        shadowAuthService.become(userId, otherUsername)
        shadowAuthService.shadow(userId, overriddenRoleSet)

        Assertions.assertThat(shadowAuthService.getRolesDirectly(userId))
            .containsExactlyInAnyOrderElementsOf(authServiceRoleSet)

    }

}