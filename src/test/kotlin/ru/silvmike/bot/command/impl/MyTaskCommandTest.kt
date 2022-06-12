package ru.silvmike.bot.command.impl

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.model.Assignment
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

private const val TEST_USER_ID = 1L

class MyTaskCommandTest: AbstractCommandTest() {

    private val assignmentDao: AssignmentDao = mockk(relaxed = true)
    private val authService: AuthService = mockk(relaxed = true)

    private val command = MyTaskCommand(assignmentDao, authService)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        clearMocks(assignmentDao, authService)
    }

    @ParameterizedTest
    @ValueSource(strings = [AuthService.USER, AuthService.ADMIN])
    fun commandUsesAssignmentDaoToReturnInformationAboutATask(role: String) {

        val testAssignment = Assignment(
            ownerId = 0,
            assigneeId = TEST_USER_ID,
            task = "TASK-123",
            createdAt = Date.from(
                ZonedDateTime.of(
                    LocalDateTime.of(2021,5,28,13,55,12), ZoneId.of(MOSCOW_TZ)
                ).toInstant()
            )
        )

        every { authService.getRoles(TEST_USER_ID) } returns setOf(role)
        every { assignmentDao.findLastByAssigneeId(TEST_USER_ID, 1) } returns sequence { yield(testAssignment) }

        command.executeCommand(TEST_USER_ID, listOf())
        verifyMessage("Список твоих задач (max=1):\nTASK-123 at 2021-05-28T13:55:12+03:00")
    }

    @Test
    fun moreThanOneAssignmentTest() {

        val assignment1 = Assignment(
            ownerId = 0,
            assigneeId = TEST_USER_ID,
            task = "TASK-123",
            createdAt = Date.from(
                ZonedDateTime.of(
                    LocalDateTime.of(2021,5,28,13,55,12), ZoneId.of(MOSCOW_TZ)
                ).toInstant()
            )
        )

        val assignment2 = Assignment(
            ownerId = 0,
            assigneeId = TEST_USER_ID,
            task = "TASK-321",
            createdAt = Date.from(
                ZonedDateTime.of(
                    LocalDateTime.of(2021,5,29,11,15,2), ZoneId.of(MOSCOW_TZ)
                ).toInstant()
            )
        )

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.USER)
        every { assignmentDao.findLastByAssigneeId(TEST_USER_ID, 1) } returns
            listOf(assignment1, assignment2).asSequence()

        command.executeCommand(TEST_USER_ID, listOf())
        verifyMessage("Список твоих задач (max=1):\n"
            + "TASK-123 at 2021-05-28T13:55:12+03:00\n"
            + "TASK-321 at 2021-05-29T11:15:02+03:00")
    }

    @Test
    fun noAssignmentTest() {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(AuthService.USER)
        every { assignmentDao.findLastByAssigneeId(TEST_USER_ID, 1) } returns sequence { }

        command.executeCommand(TEST_USER_ID, listOf())
        verifyMessage("Список твоих задач (max=1):\nУ тебя пока нет задач.")
    }

    @ParameterizedTest
    @ValueSource(strings = [AuthService.ANONYMOUS, AuthService.SUPER, "other"])
    fun onlyUserAndAdminCanDoIt(role: String) {

        every { authService.getRoles(TEST_USER_ID) } returns setOf(role)

        command.executeCommand(TEST_USER_ID, listOf())
        verifyNoMessage()
    }

}