package com.github.ipergenitsa.bot.compression

import java.io.File
import java.io.OutputStream

interface Compression {
    fun zipFilesTo(file: File, name: String, destination: File)
}