package ru.sber.io

import java.io.BufferedOutputStream
import java.io.File
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    private val bufferSize = 2048

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val logFile = File("io/logfile.log")
        val zipFile = File("io/logfile.zip")
        ZipOutputStream(zipFile.outputStream().buffered(bufferSize))
            .use { zipOut ->
                zipOut.putNextEntry(ZipEntry(logFile.name))
                logFile.inputStream().buffered(bufferSize)
                    .use { data -> data.copyTo(zipOut) }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zippedFile = File("io/logfile.zip")
        val unzippedFile = File("io/unzippedLogfile.log")

        unzippedFile.outputStream().buffered(bufferSize).use { unzipOut ->
            ZipInputStream(zippedFile.inputStream().buffered(bufferSize))
                .use { zipIn ->
                    zipIn.nextEntry
                    zipIn.copyTo(unzipOut)
                }
        }
    }
}
