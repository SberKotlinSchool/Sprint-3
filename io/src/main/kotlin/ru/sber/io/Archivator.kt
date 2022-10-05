package ru.sber.io

import java.io.File
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
            val inFile = File("logfile.log")
            val outFile = File("logfile.zip")
            inFile.inputStream().use { inStream ->
                outFile.outputStream().use { outStream ->
                    ZipOutputStream(outStream).use {
                        it.putNextEntry(ZipEntry("logfile.log"))
                        do {
                            val readByte = inStream.read()
                            if (readByte != -1) it.write(readByte)
                        } while (readByte != -1)
                    }
                }
            }
        }

        /**
         * Метод, который извлекает файл из архива.
         * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
         */
        @JvmStatic
        fun unzipLogfile() {

            val inFile = File("logfile.zip")
            val outFile = File("unzippedLogfile.log")
            inFile.inputStream().use { inStream ->
                ZipInputStream(inStream).use {
                    inZip -> if(inZip.nextEntry != null) outFile
                        .outputStream().use {
                            do {
                                val readByte = inZip.read()
                                if (readByte != -1) it.write(readByte)
                            } while (readByte != -1)
                    }
                }
            }
        }
    }
}