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
                    Выводит данное сообщение, также как и /start
                    """.trimIndent()
            )
        )

        if (isAnonymous(roles)) {

            availableCommands.add(
                Description(
                    command = "/register",
                    arguments = "<token>",
                    description = """
                    Добавляет пользователя в очередь пользователя, выдавшего <token>.
                    """.trimIndent()
                )
            )

        }

        availableCommands.add(
            Description(
                command = "/who",
                description = """
                    Возвращает user id.
                    """.trimIndent()
            )
        )

        if (isUser(roles) || isAdmin(roles)) {
            availableCommands.add(
                Description(
                    command = "/my_task",
                    arguments = "[<count>]",
                    description = """
                    Возвращает список назначенных на тебя задач. 
                        <count> - количество последних задач, которые хочется посмотреть. 
                        <count> = 1 по умолчанию.
                    """.trimIndent()
                )
            )
        }

        if (isAdmin(roles)) {
            availableCommands.add(
                Description(command = "/invite", description = """
                    Генерирует одноразовый token, который может быть использован для добавления пользователя
                    """.trimIndent())
            )

            availableCommands.add(
                Description(
                    command = "/assign",
                    arguments = "<task>",
                    description = """
                        Назначает <task> на следующего пользователя в очереди
                        """.trimIndent())
            )

            availableCommands.add(
                Description(
                    command = "/rollback",
                    description = """
                        Откатывает очередь на шаг назад.
                        """.trimIndent())
            )
        }

        if (isTheBoss(roles)) {

            availableCommands.add(
                Description(
                    command = "/invite",
                    arguments = "[<список ролей через пробел>]",
                    description = """
                        Генерирует одноразовый token, который может быть использован для добавления пользователя с заданным списком ролей.
                        Если список не указан, то подразумевается список и ролью admin.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/shadow",
                    arguments = "[<список ролей через пробел>]",
                    description = """
                        Позволяет "притвориться" пользователем с конкретным списком ролей.
                        Передача пустого списка откатывает все изменения, вызванные предыдущими вызовами /shadow.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/become",
                    arguments = "@<username>",
                    description = """
                        Позволят "притвориться" указанным пользователем.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/promote",
                    arguments = "@<username> <role>",
                    description = """
                        Позволяет добавить роль <role> пользователю <username>.
                        """.trimIndent()
                )
            )

            availableCommands.add(
                Description(
                    command = "/get_users",
                    description = """
                        Возвращает список пользователей и их роли.
                        """.trimIndent()
                )
            )
        }

        responder.respond(text = availableCommands.joinToString(separator = "\n\n"))
    }

}