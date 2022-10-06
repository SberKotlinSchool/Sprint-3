package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
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
        val reader = file.bufferedReader(Charset.forName("UTF-8"), 1024)
        val entry = ZipEntry("logfile.log")
        val zipFile = ZipOutputStream(FileOutputStream("io/logfile.zip"))
        val lines = reader.readLines()

        zipFile.putNextEntry(entry)
        val writer = zipFile.writer(Charset.forName("UTF-8"))

        writer.use { w ->
            lines.forEach { line ->
                w.write(line)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFile = ZipInputStream(FileInputStream("io/logfile.zip"))
        val reader = zipFile.bufferedReader(Charset.forName("UTF-8"))
        zipFile.nextEntry
        val lines = reader.readLines()

        val file = File("io/unzippedLogfile.log")
        val writer = file.writer(Charset.forName("UTF-8"))

        writer.use { w ->
            lines.forEach { line ->
                w.write(line)
            }
        }
    }
}