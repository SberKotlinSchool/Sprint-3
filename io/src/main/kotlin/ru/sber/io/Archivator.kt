package ru.sber.io

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

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val fileName = "io/logfile.log"
        val zipName = "io/logfile.zip"
        try {
            FileInputStream(fileName).use { fileInputStream ->
                ZipOutputStream(FileOutputStream(zipName)).use { zip ->
                    val entry = ZipEntry(fileName)
                    zip.putNextEntry(entry)
                    fileInputStream.copyTo(zip, 1024)
                }
            }
            println("Выполнено архивирование файла $fileName")
        } catch (ex: Exception) {
            println("Ошибка архивирования файла $fileName")
            throw ex
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val uuZipFileName = "io/unzippedLogfile.log"
        val zipName = "io/logfile.zip"
        try {
            ZipInputStream(FileInputStream(zipName)).use { zipInputStream ->
                while (zipInputStream.nextEntry != null) {
                    FileOutputStream(uuZipFileName).use { fileOutputStream ->
                        zipInputStream.copyTo(fileOutputStream)
                    }
                }
            }
            println("Выполнено разархивирование файла $zipName")
        } catch (ex: Exception) {
            println("Ошибка разархивирования файла $zipName")
            throw ex
        }

    }
}