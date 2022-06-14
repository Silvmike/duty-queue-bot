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
import ru.silvmike.bot.model.User

private const val TEST_SUPER_USER_ID = 123L
private const val TEST_NON_SUPER_USER_ID = 124L
private const val TEST_USERNAME = "test"
private const val TEST_USERNAME_ID = 125L

class SuspendCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val queueDao: QueueDao = mockk(relaxed = true)
    private val authService = spyk(SimpleAuthService(TEST_SUPER_USER_ID, userDao))

    private val command = SuspendCommand(userDao, queueDao, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(userDao, queueDao, authService)
        prepareMocks()
    }

    private fun prepareMocks() {
        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.ADMIN)

        val user = User(id = TEST_USERNAME_ID, username = TEST_USERNAME, roles = mutableSetOf(AuthService.USER))
        every { userDao.findByUsername(TEST_USERNAME) } returns user
    }

    @Test
    fun superUserCannotDoSuspend() {

        command.executeCommand(TEST_SUPER_USER_ID, listOf(TEST_USERNAME))
        verifyNoMessage()
    }

    @Test
    fun nonAdminCannotDoSuspend() {

        every { authService.getRoles(TEST_NON_SUPER_USER_ID) } returns setOf(AuthService.USER)
        command.executeCommand(TEST_NON_SUPER_USER_ID, listOf(TEST_USERNAME))
        verify { queueDao wasNot Called }
        verifyNoMessage()
    }

    @Test
    fun alreadySuspended() {

        val queue = DutyQueue(
            ownerId = TEST_NON_SUPER_USER_ID,
            queue = mutableListOf(TEST_NON_SUPER_USER_ID),
            suspended = setOf(TEST_USERNAME_ID))

        mockDutyQueue(queue)

        command.execute()
        verify(exactly = 1) { userDao.findByUsername(TEST_USERNAME) }
        verify(exactly = 1) { queueDao.get(TEST_NON_SUPER_USER_ID) }

        verifyMessage(text = "Участие пользователя @test в очереди дежурств уже приостановлено.")
    }

    @Test
    fun notAParticipant() {

        val queue = DutyQueue(
            ownerId = TEST_NON_SUPER_USER_ID,
            queue = mutableListOf(TEST_NON_SUPER_USER_ID),
            suspended = setOf())

        mockDutyQueue(queue)

        command.execute()
        verify(exactly = 1) { userDao.findByUsername(TEST_USERNAME) }
        verify(exactly = 1) { queueDao.get(TEST_NON_SUPER_USER_ID) }

        verifyMessage(text = "Пользователь @test не принимает участие в очереди дежурств.")
    }

    @Test
    fun doSuspend() {

        val queue = DutyQueue(
            ownerId = TEST_NON_SUPER_USER_ID,
            queue = mutableListOf(TEST_NON_SUPER_USER_ID, TEST_SUPER_USER_ID, TEST_USERNAME_ID),
            suspended = setOf())

        mockDutyQueue(queue)

        val dutyQueueSlot = slot<DutyQueue>()

        command.execute()
        verify(exactly = 1) { userDao.findByUsername(TEST_USERNAME) }
        verify(exactly = 1) { queueDao.get(TEST_NON_SUPER_USER_ID) }
        verify(exactly = 1) { queueDao.save(capture(dutyQueueSlot)) }

        Assertions.assertThat(dutyQueueSlot.captured.suspended)
            .containsExactlyElementsOf(mutableSetOf(TEST_USERNAME_ID))

        Assertions.assertThat(dutyQueueSlot.captured.queue)
            .containsExactlyElementsOf(queue.queue.minus(TEST_USERNAME_ID))

        verifyMessage(text = "Участие пользователя @test в очереди дежурств приостановлено.")
    }

    private fun mockDutyQueue(queue: DutyQueue) {
        every { queueDao.get(TEST_NON_SUPER_USER_ID) } returns queue
    }

    private fun SuspendCommand.execute() {
        executeCommand(TEST_NON_SUPER_USER_ID, listOf(TEST_USERNAME))
    }

}