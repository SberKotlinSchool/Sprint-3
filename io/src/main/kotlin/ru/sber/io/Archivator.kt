package ru.sber.io

import java.io.*
import java.util.zip.*

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    private val parentDir = "io"
    private val logFile = File(parentDir, "logfile.log")
    private val zipFile = File(parentDir, "logfile.zip")
    private val unzipFile = File(parentDir, "unzippedLogfile.log")

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        logFile.inputStream()
            .use { inputStream ->
                ZipOutputStream(zipFile.outputStream())
                    .use { zipOutputStream ->
                        val zipEntry = ZipEntry(logFile.name)
                        zipOutputStream.putNextEntry(zipEntry)
                        inputStream.copyTo(zipOutputStream)
                    }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(zipFile.inputStream())
            .use { zipInputStream ->

                while (zipInputStream.nextEntry != null) {
                    unzipFile.outputStream()
                        .use { outputStream ->
                            zipInputStream.copyTo(outputStream)
                        }
                }
            }
    }
}

