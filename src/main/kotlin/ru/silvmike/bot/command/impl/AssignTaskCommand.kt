package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.command.escaped
import ru.silvmike.bot.config.postprocessing.BotAware
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.Assignment
import ru.silvmike.bot.model.DutyQueue
import ru.silvmike.bot.model.User

class AssignTaskCommand(
    private val userDao: UserDao,
    private val queueDao: QueueDao,
    private val assignmentDao: AssignmentDao,
    private val listeners: List<Listener>,
    authService: AuthService
) : AuthorizedCommand(authService), BotAware {

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

                    val user = userDao.findById(assigneeId)!!
                    val newDutyQueue = DutyQueue(ownerId = userId, queue = queue)
                    val assignment = Assignment(ownerId = userId, assigneeId = assigneeId, task = task)

                    queueDao.save(newDutyQueue)
                    assignmentDao.save(assignment)

                    responder.respond(
                        "Пользователю @${user.username.escaped()} была назначена задача [${task.escaped()}]")

                    listeners.forEach { it.onSuccess(responder, dutyQueue, assignment, user) }

                } else {

                    responder.respond("Невозможно назначить задачу из-за отсутствия исполнителей")
                }
            }
        }
    }

    interface Listener {

        fun onSuccess(responder: Responder, dutyQueue: DutyQueue, assignment: Assignment, assignee: User)
    }

    override fun setBot(bot: Bot) {
        listeners.forEach {
            if (it is BotAware) it.setBot(bot)
        }
    }

}