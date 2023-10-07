package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    private val fName = "io/logfile.log"
    private val zipFName = "io/logfile.zip"
    private val unZipFName = "io/unZipLogfile.log"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val file = File(fName)
        val fileInputStream = file.inputStream()
        val zipFile = File(zipFName)
        val fileOutputStream = zipFile.outputStream()
        val zipOutputStream = ZipOutputStream(fileOutputStream, Charsets.UTF_8)
        val zipEntry = ZipEntry(file.name)
        zipOutputStream.putNextEntry(zipEntry)
        val buffer = ByteArray(512)
        var length: Int
        while (fileInputStream.read(buffer).also { length = it } >= 0) {
            zipOutputStream.write(buffer, 0, length)
        }
        zipOutputStream.close()
        fileInputStream.close()
        fileOutputStream.close()
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val unZipFile = File(unZipFName)
        val buffer = ByteArray(512)
        val zipInputStream = ZipInputStream(FileInputStream(zipFName))
        zipInputStream.getNextEntry()
        val fileOutputStream = unZipFile.outputStream()
        var length: Int
        while (zipInputStream.read(buffer).also { length = it } > 0) {
            fileOutputStream.write(buffer, 0, length)
        }
        zipInputStream.closeEntry()
        zipInputStream.close()
        fileOutputStream.close()
    }
}