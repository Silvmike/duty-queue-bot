package ru.silvmike.bot.dao.stub

import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.model.User

class UserDaoImpl: UserDao {

    private val byId = HashMap<Long, User>()
    private val byUsername = HashMap<String, User>()

    override fun save(user: User) {
        byId[user.id] = user
        byUsername[user.username] = user
    }

    override fun findByUsername(username: String): User? = byUsername[username]

    override fun findById(id: Long): User? = byId[id]

    override fun findAll(): Sequence<User> = byId.values.asSequence()
}