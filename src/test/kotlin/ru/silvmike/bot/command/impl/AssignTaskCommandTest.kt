package ru.silvmike.bot.command.impl

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.impl.SimpleAuthService
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue

private const val TEST_SUPER_USER_ID = 123L

class AssignTaskCommandTest: AbstractCommandTest() {

    private val userDao: UserDao = mockk(relaxed = true)
    private val queueDao: QueueDao = mockk(relaxed = true)
    private val assignmentDao: AssignmentDao = mockk(relaxed = true)
    private val authService = spyk(SimpleAuthService(TEST_SUPER_USER_ID, userDao))

    private val command = AssignTaskCommand(userDao, queueDao, assignmentDao, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(userDao, queueDao, assignmentDao, assignmentDao)
    }

    @Test
    fun assignTaskTest() {

        val userId = 1L
        val otherUserId = 2L
        val task = "TASK-12345"

        every { authService.getRoles(userId) } returns setOf(AuthService.ADMIN)

        val dutyQueue = DutyQueue(userId, mutableListOf(otherUserId, userId))
        every { queueDao.get(userId) } returns dutyQueue

        val otherUser = ru.silvmike.bot.model.User(
            id = otherUserId,
            username = "other",
            roles = mutableSetOf(AuthService.USER)
        )
        every { userDao.findById(otherUserId) } returns otherUser

        val queueSlot = slot<DutyQueue>()
        val assignmentSlot = slot<Assignment>()

        command.executeCommand(userId, listOf(task))

        verify { queueDao.save(capture(queueSlot)) }
        verify { assignmentDao.save(capture(assignmentSlot)) }

        Assertions.assertThat(queueSlot.captured.ownerId).isEqualTo(userId)
        Assertions.assertThat(queueSlot.captured.queue).containsExactlyElementsOf(listOf(userId, otherUserId))

        Assertions.assertThat(assignmentSlot.captured.ownerId).isEqualTo(userId)
        Assertions.assertThat(assignmentSlot.captured.assigneeId).isEqualTo(otherUserId)
        Assertions.assertThat(assignmentSlot.captured.task).isEqualTo(task)

        val responses = verifyMessage()
        Assertions.assertThat(responses).isNotEmpty()
        Assertions.assertThat(responses).containsExactly("User @other was assigned [TASK-12345] task")
    }

    @Test
    fun onlyAdminCanAssign() {

        val userId = 1L
        val task = "TASK-12345"

        every { authService.getRoles(userId) } returns setOf(AuthService.USER)
        command.executeCommand(userId, listOf(task))

        verify { queueDao wasNot Called }
        verify { assignmentDao wasNot Called }

    }

}