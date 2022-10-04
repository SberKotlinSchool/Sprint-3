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
    fun zipLogfile(logFilePath: String) {
        try {
            val fileName = logFilePath.split("/").last()
            print("Try to create archive for ${fileName}")
            val fileInputStream = FileInputStream(logFilePath)
            val buffer = ByteArray(fileInputStream.available())
            fileInputStream.read(buffer)
            fileInputStream.close()

            val fileOutputStream = FileOutputStream(logFilePath.replace(".log", ".zip"))
            val zipOutputStream = ZipOutputStream(fileOutputStream)
            val zipEntry = ZipEntry(fileName)
            zipOutputStream.putNextEntry(zipEntry)
            zipOutputStream.write(buffer)
            zipOutputStream.closeEntry()
            zipOutputStream.close()
        } catch (e: Exception) {
            print("Something went wrong: ${e.message}")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(logFilePath: String) {
        try {
            print("Try to unzip archive ${logFilePath.split("/").last()}")
            val zipInputStream = ZipInputStream(FileInputStream(logFilePath))
            zipInputStream.nextEntry

            val fileOutputStream = FileOutputStream(logFilePath.replace(".zip", "-unzip.log"))
            var i : Int = zipInputStream.read()
            while (i != -1) {
                fileOutputStream.write(i)
                i = zipInputStream.read()
            }
            fileOutputStream.flush()
            fileOutputStream.close()
            zipInputStream.closeEntry()
            zipInputStream.close()
        } catch (e: Exception) {
            print("Something went wrong: ${e.message}")
        }
    }
}