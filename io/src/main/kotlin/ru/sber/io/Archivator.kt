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
        try {
            ZipOutputStream(File("io/logfile.zip").outputStream()).use { outputStream ->
                File("io/logfile.log").inputStream().use { inputStream ->
                    outputStream.putNextEntry(ZipEntry("logfile.log"))
                    outputStream.write(inputStream.readAllBytes())
                }
            }
        } catch (e: Throwable) { }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        try {
            ZipInputStream(File("io/logfile.zip").inputStream()).use { inputStream ->
                inputStream.nextEntry.let {
                    File("io/unziplogfile.log").outputStream().use { outputStream ->
                        outputStream.write(inputStream.readAllBytes())
                    }
                }
            }
        } catch (e: Throwable) { }
    }
}