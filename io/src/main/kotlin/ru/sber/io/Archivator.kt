package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    private val logFile = "logfile.log"
    private val zipFile = "logfile.zip"
    private val unzippedFile = "unzippedLogfile.log"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val log = File(logFile)
        val zipped = File(log.absolutePath.replace(logFile, zipFile))
        ZipOutputStream(FileOutputStream(zipped))
            .use {
                log.copyTo(zipped, true)
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zip = File(zipFile)
        val unzipped = File(zip.absolutePath.replace(zipFile, unzippedFile))
        ZipInputStream(FileInputStream(unzipped))
            .use {
                zip.copyTo(unzipped, true)
        }
    }
}