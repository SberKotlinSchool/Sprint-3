package ru.sber.io

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    companion object {
        /**
         * Метод, который архивирует файл logfile.log в архив logfile.zip.
         * Архив должен располагаться в том же каталоге, что и исходной файл.
         */
        @JvmStatic
        fun zipLogfile() {
            val inFile = FileInputStream("logfile.log")
            val outFile = FileOutputStream("logfile.zip")
            val outZipStream = ZipOutputStream(outFile)
            val zipEntry = ZipEntry("logfile.log")
            val buffer = ByteArray(1024)
            try {
                outZipStream.putNextEntry(zipEntry)
                do {
                    val len = inFile.read(buffer)
                    if (len > 0) outZipStream.write(buffer,0, len)
                } while(len > 0)
            } catch (e: IOException) {
                println("Damn there is an error. ${e.stackTraceToString()}")
            } finally {
                outZipStream.closeEntry()
                outZipStream.close()
                inFile.close()
                outFile.close()
            }
        }

        /**
         * Метод, который извлекает файл из архива.
         * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
         */
        @JvmStatic
        fun unzipLogfile() {
            val inFile = FileInputStream("logfile.zip")
            val outFile = FileOutputStream("unzippedLogfile.log")
            val inZipStream = ZipInputStream(inFile)
            val buffer = ByteArray(1024)
            try {
                if (inZipStream.nextEntry != null) {
                    do {
                        val len = inZipStream.read(buffer)
                        if (len >= 0) outFile.write(buffer,0, len)
                    } while(len >= 0)
                }
            } catch (e: IOException) {
                println("Damn there is an error. ${e.stackTraceToString()}")
            } finally {
                inZipStream.close()
                inFile.close()
                outFile.close()
            }
        }
    }
}