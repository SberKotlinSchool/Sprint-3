package ru.sber.io

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator(val parentFolder: String = "/") {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val inputFileName = "logfile.log"
        val inputFile = File(parentFolder, inputFileName)

        val outputFileName = "logfile.zip"
        val outputFile = File(parentFolder, outputFileName)

        ZipOutputStream(outputFile.outputStream()).use { outputStream ->
            inputFile.inputStream().use { inputStream ->
                val entry = ZipEntry(inputFile.name)
                outputStream.putNextEntry(entry)
                inputStream.copyTo(outputStream, 1024)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val inputFileName = "logfile.zip"
        val inputFile = File(parentFolder, inputFileName)

        val outputFileName = "unzippedLogfile.log"
        val outputFile = File(parentFolder, outputFileName)

        ZipInputStream(inputFile.inputStream()).use { inputStream ->
            outputFile.outputStream().use { outputStream ->
                inputStream.nextEntry
                inputStream.copyTo(outputStream, 1024)
            }
        }
    }
}