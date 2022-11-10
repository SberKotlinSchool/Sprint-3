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
        val fileSource = File("io/logfile.log")
        val fileTarget = File("io/logfile.zip")

        ZipOutputStream(fileTarget.outputStream()).use { output ->
            fileSource.inputStream().use { input ->
                output.putNextEntry(ZipEntry("logfile.log"))
                output.write(input.readAllBytes())
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val fileSource = File("io/logfile.zip")
        val fileTarget = File("io/newLogfile.log")

        ZipInputStream(fileSource.inputStream()).use { input ->
            fileTarget.outputStream().use { output ->
                input.nextEntry
                input.copyTo(output)
            }
        }

    }
}