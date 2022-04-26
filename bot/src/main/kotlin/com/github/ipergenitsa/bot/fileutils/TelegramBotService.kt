package com.github.ipergenitsa.bot.fileutils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.generics.BotSession

@Service
class TelegramBotService(telegramBotsApi: TelegramBotsApi, fileCompressionBot: FileCompressionBot): DisposableBean {

    private val botSession: BotSession = telegramBotsApi.registerBot(fileCompressionBot)

    override fun destroy() {
        log.info("Stopping The Bot...")
        botSession.stop()
        log.info("The Bot has been stopped")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(TelegramBotService::class.java)
    }
}