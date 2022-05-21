package ru.silvmike.bot.dao.api

import ru.silvmike.bot.model.Assignment

interface AssignmentDao {

    fun save(assignment: Assignment)

    fun findLastByAssigneeId(assigneeId: Long, count: Int = 1): Sequence<Assignment>

}