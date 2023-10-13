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
    fun zipLogfile(file: File) {
        val output = File("${file.parent}/${file.nameWithoutExtension}.zip")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(output))).use { zipOutputStream ->
            zipOutputStream.putNextEntry(ZipEntry(file.name))
            file.inputStream().use { it.copyTo(zipOutputStream) }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(file: File) {
        val output = File("${file.parent}/unzippedLogfile.log").outputStream()
        ZipInputStream(FileInputStream(file))
                .use { zipInputStream ->
                    generateSequence { zipInputStream.nextEntry }
                            .firstOrNull()?.let { zipInputStream.copyTo(output) }
                }
    }
}