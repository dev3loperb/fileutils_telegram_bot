package com.github.ipergenitsa.bot.fileutils

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi

@Service
class TelegramBotService(telegramBotsApi: TelegramBotsApi, fileCompressionBot: FileCompressionBot) {
    init {
        telegramBotsApi.registerBot(fileCompressionBot)
    }
}