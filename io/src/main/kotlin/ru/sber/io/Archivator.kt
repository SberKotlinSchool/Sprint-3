package ru.sber.io

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    private val baseDir = "io"
    private val logFile = "logfile.log"
    private val zipFile = "logfile.zip"
    private val unZipFile = "unzippedLogfile.log"
    private val bufferSize = 1024

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        File(baseDir, logFile).inputStream().use { inStream ->
                ZipOutputStream(File(baseDir, zipFile).outputStream()).use {
                outStream -> outStream.putNextEntry(ZipEntry(logFile))
                inStream.copyTo(outStream, bufferSize)
                outStream.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(File(baseDir, zipFile).inputStream()).use { inStream ->
            while (inStream.nextEntry != null) {
                File(baseDir, unZipFile).outputStream().use {outStream ->
                    inStream.copyTo(outStream, bufferSize)
                }
            }
        }
    }

}

fun main() {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}