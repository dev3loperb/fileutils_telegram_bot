package com.github.ipergenitsa.bot.fileutils

import com.github.ipergenitsa.bot.compression.Compression
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.notExists


@Component
class FileCompressionBot(
    private val botSecurityConfig: BotSecurityConfig,
    private val zipCompression: Compression
) : TelegramLongPollingBot() {

    override fun getBotUsername(): String {
        return "compression bot"
    }

    override fun getBotToken(): String {
        return botSecurityConfig.token
    }

    override fun onUpdateReceived(update: Update) {
        val message = update.takeIf { it.hasMessage() }?.message
        log.info("Handling msg: {}", message?.messageId)
        message?.takeIf { it.hasDocument() }?.document?.let { document ->
            try {
                val fileName = document.fileName

                val downloadedFile = downloadDocument(document)!!
                val tmpFile = filesTmpDir().resolve("$fileName.zip").toFile()
                zipCompression.zipFilesTo(downloadedFile, fileName, tmpFile)

                val response = SendDocument().apply {
                    chatId = message.chatId.toString()
                    this.document = InputFile(tmpFile)
                    caption = "Your file is compressed"
                }

                execute(response) // Call method to send the message
            } catch (e: TelegramApiException) {
                log.error("error sending the response to the user", e)
            }
        }
    }

    private fun filesTmpDir(): Path {
        val dir = Path.of("telegram-bot")
        if (dir.notExists()) {
            dir.createDirectory()
        }
        return dir
    }

    private fun downloadDocument(document: Document): File? {
        val getFileRequest = GetFile(document.fileId)
        val telegramFile = execute(getFileRequest)
        val inputFileAsStream = downloadFileAsStream(telegramFile)
        val destination = filesTmpDir().resolve("input").resolve("tmp${document.fileId}").toFile()
        FileUtils.copyInputStreamToFile(
            inputFileAsStream,
            destination
        )
        return destination
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(FileCompressionBot::class.java)
    }
}