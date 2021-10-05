package org.telegram.bot.fileutilsbot

import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


@Component
class FileCompressionBot(private val botSecurityConfig: BotSecurityConfig) : TelegramLongPollingBot() {
    override fun getBotUsername(): String {
        return "compression bot"
    }

    override fun getBotToken(): String {
        return botSecurityConfig.token
    }

    override fun onUpdateReceived(update: Update?) {
        if (update == null) {
            return
        }
        val message = update.takeIf { it.hasMessage() }?.message
        val document = message?.takeIf { it.hasDocument() }?.document
        document?.let {
            val response = SendMessage().apply {
                chatId = message.chatId.toString()
                text = "Your file is ready"
            }
            try {
                execute(response) // Call method to send the message
            } catch (e: TelegramApiException) {
                println("error sending the response to the user $e")
            }
        }
    }
}