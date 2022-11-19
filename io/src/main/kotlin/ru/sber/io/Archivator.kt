package ru.sber.io

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
class Archivator {

    private val nameFile: String = "logfile.log"
    private val zipFile: String = "io/logfile.zip"
    private val file = File("io/$nameFile")
    private val unzipFileLog: String = "io/unzippedLogfile.log"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val lines = file.bufferedReader().readLines()
        val zos = ZipOutputStream(FileOutputStream(zipFile))
        val zipEntry = ZipEntry(nameFile)
        zos.use { stream ->
            stream.putNextEntry(zipEntry)
            val bufWriter = stream.bufferedWriter()
            lines.forEach { str -> bufWriter.write(str + "\n") }
            bufWriter.flush()
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохранить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zis = ZipInputStream(FileInputStream(zipFile))
        val file = File(unzipFileLog)
        zis.use {
            it.nextEntry
            it.copyTo(file.outputStream())
        }
    }
}