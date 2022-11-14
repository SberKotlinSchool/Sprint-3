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
    fun zipLogfile(path : String) {
        val fileInputStream = FileInputStream(path)

        val byteArrayBuffer = ByteArray(fileInputStream.available())
        fileInputStream.read(byteArrayBuffer)
        fileInputStream.close()

        val zipOutputStream = ZipOutputStream(FileOutputStream(path.replace(".log" , ".zip")))
        val zipEntry = ZipEntry(path)
        zipOutputStream.putNextEntry(zipEntry)
        zipOutputStream.write(byteArrayBuffer)
        zipOutputStream.closeEntry()
        zipOutputStream.close()
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(path: String) {
        val zipInputStream = ZipInputStream(FileInputStream(path))
        zipInputStream.nextEntry
        val logFileName = path.split("\\").last()
        val unzippedLogFileName = "unzipped" + logFileName.replace(".zip" , ".log")
        val fileOutputStream = FileOutputStream(path.replace(logFileName, unzippedLogFileName))

        var nextByte: Int = zipInputStream.read()
        while (nextByte != -1){
            fileOutputStream.write(nextByte)
            nextByte = zipInputStream.read()
        }

        fileOutputStream.flush()
        fileOutputStream.close()
        zipInputStream.closeEntry()
        zipInputStream.close()
    }
}