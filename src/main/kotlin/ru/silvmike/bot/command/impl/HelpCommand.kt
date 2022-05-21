package ru.silvmike.bot.command.impl

import com.github.kotlintelegrambot.entities.Message
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.command.api.Responder
import ru.silvmike.bot.command.impl.help.Description

class HelpCommand(authService: AuthService): AuthorizedCommand(authService) {

    override fun execute(responder: Responder, message: Message, arguments: List<String>, roles: Set<String>) {
        val availableCommands = mutableListOf<Description>()

        availableCommands.add(
            Description(
                command = "/help",
                description = """
                    Prints this message as well as /start does.
                    """.trimIndent()
            )
        )

        if (isAnonymous(roles)) {

            availableCommands.add(
                Description(
                    command = "/register",
                    arguments = "<token>",
                    description = """
                    Registers you to the work queue of the given <token> issuer.
                    """.trimIndent()
                )
            )

        }

        availableCommands.add(
            Description(
                command = "/who",
                description = """
                    Returns your user id.
                    """.trimIndent()
            )
        )

        if (isUser(roles) || isAdmin(roles)) {
            availableCommands.add(
                Description(
                    command = "/my_task",
                    arguments = "[<count>]",
                    description = """
                    Returns list of tasks which were assigned to you. 
                        <count> - is the max count of tasks you want to retrieve. 
                        <count> = 1 by default.
                    """.trimIndent()
                )
            )
        }

        if (isAdmin(roles)) {
            availableCommands.add(
                Description(command = "/invite", description = """
                    Generates one-time token that can be used by Telegram user to register
                    """.trimIndent())
            )

            availableCommands.add(
                Description(
                    command = "/assign",
                    arguments = "<task>",
                    description = """
                        Assigns a <task> to the next user in a queue
                        """.trimIndent())
            )

            availableCommands.add(
                Description(
                    command = "/rollback",
                    description = """
                        Rolls back the duty queue.
                        """.trimIndent())
            )
        }

        if (isTheBoss(roles)) {

            availableCommands.add(
                Description(
                    command = "/invite",
                    arguments = "[<space-separated list of roles>]",
                    description = """
                        Generates one-time token that can be used by Telegram user to register with a set of specified roles.
                        When list of roles is omitted, it assumes that it contains a single admin role.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/shadow",
                    arguments = "[<space-separated list of roles>]",
                    description = """
                        Makes bot behave like you have set of roles you've specified.
                        Empty list reverts changes caused by previous calls to /shadow.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/become",
                    arguments = "@<username>",
                    description = """
                        Makes bot behave as it would if you were the specified user.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/promote",
                    arguments = "@<username> <role>",
                    description = """
                        Adds a <role> to a user with the specified <username>.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/get_users",
                    description = """
                        Returns list of all users and their roles.
                        """.trimIndent()
                )
            )
        }

        responder.respond(text = availableCommands.joinToString(separator = "\n\n"))
    }

}