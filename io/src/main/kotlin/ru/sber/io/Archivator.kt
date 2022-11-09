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

        val file = File("io/logfile.log")
        val zipFile = File("io/logfile.zip")

        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
            FileInputStream(file).use { input ->
                BufferedInputStream(input).use { origin ->
                    val entry = ZipEntry(file.name)
                    output.putNextEntry(entry)
                    origin.copyTo(output, 1024)
                }
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {

        val zipFile = File("io/logfile.zip")
        val unzipped = File("io/unzippedLogfile.log")

        BufferedOutputStream(FileOutputStream(unzipped)).use { output ->
            ZipInputStream(BufferedInputStream(FileInputStream(zipFile))).use { zip ->
                zip.nextEntry
                zip.copyTo(output)
            }
        }
    }
}