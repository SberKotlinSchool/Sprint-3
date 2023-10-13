package ru.sber.io

import java.io.File
import java.io.FileOutputStream
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
    fun zipLogfile(fileName: String) {
        File(fileName).inputStream().use { inputStream ->
            val zippedFile = fileName.removeSuffix(".log").plus(".zip")
            ZipOutputStream(FileOutputStream(zippedFile)).use {
                it.putNextEntry(ZipEntry(fileName.split("/").last()))
                inputStream.copyTo(it)
                it.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(archiveName: String) {
        val unzippedFile = "io/unzippedLogfile.log"
        ZipInputStream(File(archiveName).inputStream()).use { inputStream ->
            File(unzippedFile).outputStream().use { out ->
                inputStream.nextEntry
                inputStream.copyTo(out)
            }
        }
    }
}

fun main() {
    val archivator = Archivator()
    archivator.zipLogfile("io/logfile.log")
    archivator.unzipLogfile("io/logfile.zip")
}