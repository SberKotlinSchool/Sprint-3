package ru.sber.io

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
fun main() {
    Archivator().zipLogfile(File("io/logfile.log"))
    Archivator().unzipLogfile(File("io/logfile.zip"))
}

class Archivator {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile(sourceFile: File) {
        val outputFile = File("${sourceFile.parent}/${sourceFile.nameWithoutExtension}.zip")
        try {
            ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { zipOutputStream ->
                zipOutputStream.putNextEntry(ZipEntry(sourceFile.name))
                sourceFile.inputStream().use { it.copyTo(zipOutputStream) }
            }
        } catch (e: Exception) {
            println("Error occurred while zipping the file: ${e.message}")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(zipFile: File) {
        val output = File("${zipFile.parent}/unzippedLogfile.log").outputStream()
        try {
            ZipInputStream(FileInputStream(zipFile)).use { zipInputStream ->
                generateSequence { zipInputStream.nextEntry }
                    .firstOrNull()?.let { zipInputStream.copyTo(output) }
            }
        } catch (e: Exception) {
            println("Error occurred while unzipping the file: ${e.message}")
        }
    }
}