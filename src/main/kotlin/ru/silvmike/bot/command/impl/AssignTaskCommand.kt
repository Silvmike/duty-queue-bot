package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue

class AssignTaskCommand(
    private val userDao: UserDao,
    private val queueDao: QueueDao,
    private val assignmentDao: AssignmentDao,
    authService: AuthService
) : AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        if (isAdmin(roles)) {

            val task = arguments.getOrNull(0) ?: ""

            if (task.isNotEmpty()) {

                val userId = message.from!!.id
                val dutyQueue = queueDao.get(userId)

                if (dutyQueue.queue.isNotEmpty()) {

                    val queue = dutyQueue.queue.toMutableList()
                    val assigneeId = queue.removeFirst()
                    queue.add(assigneeId)

                    val user = userDao.findById(assigneeId)

                    queueDao.save(DutyQueue(ownerId = userId, queue = queue))
                    assignmentDao.save(Assignment(ownerId = userId, assigneeId = assigneeId, task = task))

                    responder.respond("Пользователю @${user?.username} бала назначена задача [${task}]")
                }
            }
        }
    }
}