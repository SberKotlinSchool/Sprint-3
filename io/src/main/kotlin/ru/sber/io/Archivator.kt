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

    private val fileName = "io/logfile.log"
    private val zipFileName = "io/logfile.zip"
    private val unZipFileName = "io/unzippedLogfile.log"
    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val source = File(fileName)
        val fis = source.inputStream()

        val destination = File(zipFileName)
        val fos = destination.outputStream()
        val zipOutputStream = ZipOutputStream(fos, Charsets.UTF_8)
        val zipEntry = ZipEntry(source.name)
        zipOutputStream.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        var length: Int
        while (fis.read(bytes).also { length = it } >= 0) {
            zipOutputStream.write(bytes, 0, length)
        }
        zipOutputStream.close()
        fis.close()
        fos.close()
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val destination = File(unZipFileName)
        val buffer = ByteArray(1024)
        val zipInputStream = ZipInputStream(FileInputStream(zipFileName))
        val fos = destination.outputStream()
        var length: Int
        while (zipInputStream.read(buffer).also { length = it } > 0) {
            fos.write(buffer, 0, length)
        }
        zipInputStream.closeEntry()
        zipInputStream.close()
        fos.close()
    }
}

fun main () {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()

}