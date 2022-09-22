package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    // Поместил методы сюда, чтобы можно было вызывать их
    // без создания экземпляра класса Archivator
    companion object {
        /**
         * Метод, который архивирует файл logfile.log в архив logfile.zip.
         * Архив должен располагаться в том же каталоге, что и исходной файл.
         */
        fun zipLogfile(logFileName: String, zipFileName: String) {
            val inputFile = File("io/$logFileName.log")
            val outputFile = File("io/$zipFileName.zip")
            ZipOutputStream(FileOutputStream(outputFile)).use { output ->
                FileInputStream(inputFile).use { input ->
                    val entry = ZipEntry(inputFile.name)
                    output.putNextEntry(entry)
                    input.copyTo(output, 1024)
                }
            }
        }

        /**
         * Метод, который извлекает файл из архива.
         * Извлечь из архива logfile.zip файл и сохранить его в том же каталоге с именем unzippedLogfile.log
         */
        fun unzipLogfile(zipFileName: String, logFileName: String) {
            val inputFile = File("io/$zipFileName.zip")
            val outputFile = File("io/$logFileName.log")
            ZipInputStream(FileInputStream(inputFile)).use { input ->
                var entry = input.nextEntry
                while (entry != null) {
                    FileOutputStream(outputFile).use { output ->
                        val buffer = ByteArray(1024)
                        var len = input.read(buffer)
                        while (len != -1) {
                            output.write(buffer, 0, len)
                            len = input.read(buffer)
                        }
                        input.closeEntry()
                    }
                    entry = input.nextEntry
                }
            }
        }
    }
}

fun main() {
    Archivator.zipLogfile("logfile", "logfile")
    Archivator.unzipLogfile("logfile", "unzippedLogfile")
}