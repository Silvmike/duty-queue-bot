package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.DutyQueue

private const val TEST_SUPER_USER_ID = 123L
private const val TEST_NON_SUPER_USER_ID = 124L

class RollbackCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val authService = spyk(SimpleAuthService(TEST_SUPER_USER_ID, userDao))
    private val queueDao: QueueDao = mockk(relaxed = true)

    private val command = RollbackCommand(queueDao, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(userDao, authService, queueDao)
    }

    @Test
    fun superUserCannotDoRollback() {

        command.executeCommand(TEST_SUPER_USER_ID)
        verifyNoMessage()
    }

    @Test
    fun nonAdminCannotDoRollback() {

        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.USER)
        command.executeCommand(TEST_NON_SUPER_USER_ID)
        verify { queueDao wasNot Called }
        verifyNoMessage()
    }

    @Test
    fun doRollback() {

        val queue = DutyQueue(TEST_NON_SUPER_USER_ID, mutableListOf(1, 2, 3), setOf())
        val expectedQueue: MutableList<Long> = mutableListOf(3, 1, 2)

        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.ADMIN)
        every { queueDao.get(TEST_NON_SUPER_USER_ID) } returns queue

        val dutyQueueSlot = slot<DutyQueue>()

        command.executeCommand(TEST_NON_SUPER_USER_ID)
        verify(exactly = 1) { queueDao.get(TEST_NON_SUPER_USER_ID) }
        verify(exactly = 1) { queueDao.save(capture(dutyQueueSlot)) }

        Assertions.assertThat(dutyQueueSlot.captured.queue).containsExactlyElementsOf(expectedQueue)
        verifyMessage(text = "Колесо было откачено на один шаг.")
    }

}