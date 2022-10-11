package ru.sber.io

import java.io.File
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    private val FILENAME = "./io/logfile.log"
    private val UNZIPPED_FILENAME = "./io/unzippedLogfile.log"
    private val ARCHIVENAME = FILENAME.replace(".log", ".zip")

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {

        try {
            ZipOutputStream(File(ARCHIVENAME).outputStream()).use { writer ->
                val file = File(FILENAME)
                val zipFile = ZipEntry(file.name)

                writer.putNextEntry(zipFile)

                file.inputStream().use {
                    var readByte = it.read()
                    while (readByte != -1) {
                        writer.write(readByte)
                        readByte = it.read()
                    }
                }
            }
        } catch (exception: IOException) {
            println("Something went wrong: ${exception.message}")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val unzippedFile = File(UNZIPPED_FILENAME)

        try {
            ZipInputStream(File(ARCHIVENAME).inputStream()).use {
                it.nextEntry

                unzippedFile.outputStream().use { writer ->
                    var readByte = it.read()
                    while (readByte != -1) {
                        writer.write(readByte)
                        readByte = it.read()
                    }
                }
            }
        } catch (exception: IOException) {
            println("Something went wrong: ${exception.message}")
        }
    }
}