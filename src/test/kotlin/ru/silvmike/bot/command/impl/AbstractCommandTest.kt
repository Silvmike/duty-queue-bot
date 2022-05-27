package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Chat
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.User
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import ru.silvmike.bot.command.api.Command
import ru.silvmike.bot.command.api.Responder

abstract class AbstractCommandTest {

    private val responder: Responder = mockk(relaxed = true)

    @BeforeEach
    open fun setUp() {
        clearMocks(responder)
    }

    protected fun verifyMessage(text: String) {
        verify { responder.respond(text = text) }
    }

    protected fun verifyMessage(): List<String> {
        val result = mutableListOf<String>()
        verify { responder.respond(text = capture(result)) }
        return result
    }

    protected fun verifyNoMessage() {
        verify { responder wasNot Called }
    }

    protected fun Command.executeCommand(userId: Long, arguments: List<String> = listOf()) {

        execute(
            responder,
            Message(
                messageId = 1L,
                date = 2L,
                chat = Chat(id = 2, type = "user"),
                from = User(id = userId, isBot = false, firstName = "John")
            ),
            arguments
        )
    }

}