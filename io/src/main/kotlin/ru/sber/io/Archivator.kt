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
        try {
            val input = FileInputStream("io/logfile.log")
            val entry = ZipEntry("logfile.log")
            val output = ZipOutputStream(FileOutputStream("io/logfile.zip"))
            output.putNextEntry(entry)
            val buffer = ByteArray(1024)
            while (true) {
                val len = input.read(buffer)
                if (len < 0)
                    break
                output.write(buffer, 0, len)
            }
        } catch (throwable: Throwable) {
            println("Stack trace:\n ${throwable.stackTraceToString()}")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        try {
            val input = ZipInputStream(FileInputStream("io/logfile.zip"))
            val output = FileOutputStream("io/unzippedLogfile.log")
            val buffer = ByteArray(1024)
            input.nextEntry
            while (true) {
                val len = input.read(buffer)
                if (len < 0)
                    break
                output.write(buffer, 0, len);
            }
        } catch (throwable: Throwable) {
            println("Stack trace:\n ${throwable.stackTraceToString()}")
        }
    }
}