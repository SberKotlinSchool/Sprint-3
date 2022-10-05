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
        val path = "logs$logFilePath"
        val fileInputStream = FileInputStream(path)

        val buffer = ByteArray(fileInputStream.available())
        fileInputStream.read(buffer)
        fileInputStream.close()

        val zipOutputStream = ZipOutputStream(FileOutputStream(path.replace(".log", ".zip")))
        val zipEntry = ZipEntry(logFilePath.split("/").last())
        zipOutputStream.putNextEntry(zipEntry)
        zipOutputStream.write(buffer)
        zipOutputStream.closeEntry()
        zipOutputStream.close()

    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(logFilePath: String) {
        val path = "logs$logFilePath"

        val zipInputStream = ZipInputStream(FileInputStream(path))
        zipInputStream.nextEntry
        val fileOutputStream = FileOutputStream(path.replace(".zip", "-unzip.log"))

        var i : Int = zipInputStream.read()
        while (i != -1) {
            fileOutputStream.write(i)
            i = zipInputStream.read()
        }

        fileOutputStream.flush()
        fileOutputStream.close()

        zipInputStream.closeEntry()
        zipInputStream.close()
    }
}