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
        val fileName = "logfile.log"
        val zipFileName = "logfile.zip"
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFileName))).use { fos ->
            val file = File(fileName)
            file.inputStream().use { fis ->
                val zipEntry = ZipEntry(zipFileName)
                fos.putNextEntry(zipEntry)
                fis.copyTo(fos)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFileName = "logfile.zip"
        val unZipFileName = "unzippedLogfile.log"
        ZipInputStream(BufferedInputStream(FileInputStream(zipFileName))).use { zip ->
            val file = File(unZipFileName)
            file.outputStream().use { fos ->
                var entry = zip.nextEntry
                while (entry != null) {
                    zip.copyTo(fos)
                    entry = zip.nextEntry
                }
            }
        }
    }
}