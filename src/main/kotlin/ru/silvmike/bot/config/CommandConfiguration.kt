package ru.silvmike.bot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.silvmike.bot.auth.api.AuthService
import ru.silvmike.bot.auth.api.BecomeService
import ru.silvmike.bot.auth.api.ShadowService
import ru.silvmike.bot.command.impl.*
import ru.silvmike.bot.dao.api.AssignmentDao
import ru.silvmike.bot.dao.api.QueueDao
import ru.silvmike.bot.dao.api.TokenDao
import ru.silvmike.bot.dao.api.UserDao
import ru.silvmike.bot.dispatcher.BotCommandConfigurer
import ru.silvmike.bot.service.TokenGenerator

@Configuration
open class CommandConfiguration {

    @Bean
    open fun registerCommand(tokenDao: TokenDao, userDao: UserDao, queueDao: QueueDao) =
        BotCommandConfigurer("register", RegisterCommand(tokenDao, userDao, queueDao))

    @Bean
    open fun shadowCommand(shadowService: ShadowService, authService: AuthService) =
        BotCommandConfigurer("shadow", ShadowCommand(shadowService, authService))

    @Bean
    open fun becomeCommand(becomeService: BecomeService, authService: AuthService) =
        BotCommandConfigurer("become", BecomeCommand(becomeService, authService))

    @Bean
    open fun echoCommand() =
        BotCommandConfigurer("echo", EchoCommand())

    @Bean
    open fun whoAmICommand(becomeService: BecomeService, authService: AuthService) =
        BotCommandConfigurer("who", WhoAmICommand(becomeService, authService))

    @Bean
    open fun getUsersCommand(userDao: UserDao, authService: AuthService) =
        BotCommandConfigurer("get_users", GetUsersCommand(userDao, authService))

    @Bean
    open fun inviteCommand(tokenDao: TokenDao, tokenGenerator: TokenGenerator, authService: AuthService) =
        BotCommandConfigurer("invite", InviteCommand(tokenDao, tokenGenerator, authService))

    @Bean
    open fun promoteCommand(userDao: UserDao, authService: AuthService) =
        BotCommandConfigurer("promote", PromoteCommand(userDao, authService))

    @Bean
    open fun assignCommand(userDao: UserDao, queueDao: QueueDao, assignmentDao: AssignmentDao, authService: AuthService) =
        BotCommandConfigurer("assign", AssignTaskCommand(userDao, queueDao, assignmentDao, authService))

    @Bean
    open fun suspendCommand(userDao: UserDao, queueDao: QueueDao, authService: AuthService) =
        BotCommandConfigurer("suspend", SuspendCommand(userDao, queueDao, authService))

    @Bean
    open fun rollbackCommand(queueDao: QueueDao, authService: AuthService) =
        BotCommandConfigurer("rollback", RollbackCommand(queueDao, authService))

    @Bean
    open fun myTaskCommand(assignmentDao: AssignmentDao, authService: AuthService) =
        BotCommandConfigurer("my_task", MyTaskCommand(assignmentDao, authService))

    @Bean
    open fun helpCommand(authService: AuthService) =
        BotCommandConfigurer("help", HelpCommand(authService))

    @Bean
    open fun startCommand(authService: AuthService) =
        BotCommandConfigurer("start", HelpCommand(authService))

}