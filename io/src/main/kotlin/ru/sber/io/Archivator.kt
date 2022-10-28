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

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val logFile = File("io/logfile.log")
        val zipFile = File("io/logfile.zip")
        ZipOutputStream(BufferedOutputStream(zipFile.outputStream(), 2048))
            .use { zipOut ->
                zipOut.putNextEntry(ZipEntry(logFile.name))
                logFile.inputStream().buffered(2048)
                    .use { data -> data.copyTo(zipOut) }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {

    }
}
