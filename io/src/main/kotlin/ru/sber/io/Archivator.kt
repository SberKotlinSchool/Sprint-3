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
        val sourceFile = File("./io/logfile.log")
        val zipFile = File("./io/logfile.zip")

        ZipOutputStream(zipFile.outputStream()).use { writer ->
            sourceFile.inputStream().use { reader ->
                writer.putNextEntry(ZipEntry(sourceFile.name))
                reader.copyTo(writer)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFile = File("./io/logfile.zip")
        val unzipFile = File("./io/unzippedLogfile.log")

        ZipInputStream(zipFile.inputStream()).use { reader ->
            unzipFile.outputStream().use { writer ->
                reader.nextEntry
                reader.copyTo(writer)
            }
        }

    }


}