package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.DutyQueue

private const val TEST_SUPER_USER_ID = 123L
private const val TEST_NON_SUPER_USER_ID = 124L

class RollbackCommandTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService = spyk(SimpleAuthService(TEST_SUPER_USER_ID, userDao))
    private val queueDao: QueueDao = mockk(relaxed = true)
    private val command = RollbackCommand(queueDao, authService)

    private val responder: Responder = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        clearMocks(userDao, authService, queueDao, responder)
    }

    @Test
    fun superUserCannotDoRollback() {

        command.executeCommand(responder, TEST_SUPER_USER_ID)
        verify { queueDao wasNot Called }
    }

    @Test
    fun nonAdminCannotDoRollback() {

        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.USER)
        command.executeCommand(responder, TEST_NON_SUPER_USER_ID)
        verify { queueDao wasNot Called }
    }

    @Test
    fun doRollback() {

        val queue = DutyQueue(TEST_NON_SUPER_USER_ID, mutableListOf(1, 2, 3))
        val expectedQueue: MutableList<Long> = mutableListOf(3, 1, 2)

        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.ADMIN)
        every { queueDao.get(TEST_NON_SUPER_USER_ID) } returns queue

        val dutyQueueSlot = slot<DutyQueue>()
        every { queueDao.save(capture(dutyQueueSlot)) }

        command.executeCommand(responder, TEST_NON_SUPER_USER_ID)
        verify(exactly = 1) { queueDao.get(TEST_NON_SUPER_USER_ID) }
        verify(exactly = 1) { queueDao.save(any()) }

        Assertions.assertThat(dutyQueueSlot.captured.queue).containsExactlyElementsOf(expectedQueue)
    }

}