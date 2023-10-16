package ru.sber.io

import java.io.*
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
        val inputFileName = "io/logfile.log"
        val outputArchiveName = "io/logfile.zip"

        val zipOutputStream = ZipOutputStream(FileOutputStream(outputArchiveName))

        try {
            zipOutputStream.use { stream ->
                stream.putNextEntry(ZipEntry("logfile.log"))
                stream.write(File(inputFileName).readBytes())
                stream.closeEntry()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val inputArchiveName = "io/logfile.zip"
        val outputFileName = "io/unzippedLogfile.log"

        try {
            FileOutputStream(outputFileName).use { file ->
                ZipInputStream(FileInputStream(inputArchiveName)).use { input ->
                    if (input.nextEntry != null) input.copyTo(file)
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}

fun main() {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}