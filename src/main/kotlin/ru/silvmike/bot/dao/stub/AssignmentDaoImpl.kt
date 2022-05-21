package ru.silvmike.bot.dao.stub

import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.model.Assignment

class AssignmentDaoImpl: AssignmentDao {

    private val storage = HashMap<Long, MutableList<Assignment>>()

    override fun save(assignment: Assignment) {
        storage.computeIfAbsent(assignment.ownerId) { mutableListOf() }.add(assignment)
    }

    override fun findLastByAssigneeId(assigneeId: Long, count: Int): Sequence<Assignment> =
        storage.entries.asSequence()
            .flatMap {
                it.value.asSequence()
                    .filter { assignment -> assignment.assigneeId == assigneeId }
                    .toList().let { input ->

                        input.subList(
                            Math.max(input.size - count, 0),
                            input.size
                        )
                    }.asSequence()
            }

}