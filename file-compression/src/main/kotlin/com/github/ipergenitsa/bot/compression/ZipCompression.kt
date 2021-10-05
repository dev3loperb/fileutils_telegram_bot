package com.github.ipergenitsa.bot.compression

import java.io.File
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class ZipCompression : Compression {

    override fun zipFilesTo(file: File, name: String, destination: File) {
        destination.outputStream().use { outputStream ->
            ZipOutputStream(outputStream).use { zipOutputStream ->
                zipOutputStream.putNextEntry(ZipEntry(name))
                Files.copy(file.toPath(), zipOutputStream)
                zipOutputStream.closeEntry()
            }
        }
    }
}