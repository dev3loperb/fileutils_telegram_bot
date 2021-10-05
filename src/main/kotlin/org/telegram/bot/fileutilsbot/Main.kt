package org.telegram.bot.fileutilsbot

import com.typesafe.config.ConfigFactory
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


fun main() {
    try {
        val config = ConfigFactory.load()
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(FileCompressionBot(parseBotSecurityConfig(config)))
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
}