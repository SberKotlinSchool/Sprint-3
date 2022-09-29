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

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val file = File("io/logfile.log")
        val zip = File("io/logfile.zip")

        try {
            ZipOutputStream(zip.outputStream()).use { zos ->
                file.inputStream().use {
                    zos.putNextEntry(ZipEntry("logfile.log"))
                    zos.write(it.readAllBytes())
                }
            }
        } catch (throwable: Throwable) {
            println("Got error:\n ${throwable.stackTraceToString()}")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val unzip = File("io/unzippedLogfile.log")
        val zip = File("io/logfile.zip")

        try {
            ZipInputStream(zip.inputStream()).use { zis ->
                zis.nextEntry?.let {
                    unzip.outputStream().use {
                        it.write(zis.readAllBytes())
                    }
                }
            }
        } catch (throwable: Throwable) {
            println("Got error:\n ${throwable.stackTraceToString()}")
        }
    }
}
