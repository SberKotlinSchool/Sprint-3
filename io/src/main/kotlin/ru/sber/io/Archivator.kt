package ru.sber.io

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
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
    private val fileName = "logfile"
    val logFileName = "$fileName.log"
    val zipFileName = "$fileName.zip"

    fun zipLogfile() {
        FileOutputStream(zipFileName).use { fileOutputStream ->
            ZipOutputStream(fileOutputStream).use { zipOutputStream ->
                zipOutputStream.putNextEntry(ZipEntry(logFileName))
                FileInputStream(logFileName).use { it.copyTo(zipOutputStream) }
                zipOutputStream.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        FileInputStream(zipFileName).use { fileInputStream ->
            ZipInputStream(fileInputStream).use { zipInputStream ->
                var zipEntry = zipInputStream.nextEntry
                while (zipEntry != null) {
                    if (logFileName == zipEntry.name) {
                        FileOutputStream("unzipped${zipEntry.name.myCapitalize()}").use { zipInputStream.copyTo(it) }
                    }
                    zipEntry = zipInputStream.nextEntry
                }
            }
        }
    }

    private fun String.myCapitalize(): String {
        return this.replaceFirstChar { it.titlecase(Locale.getDefault()) }
    }
}