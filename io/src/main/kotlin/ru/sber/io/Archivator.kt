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

        val inputFile = File("io/logfile.log")
        val outputFile = File("io/logfile.zip")

        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { zos ->
            BufferedInputStream(FileInputStream(inputFile)).use { bis ->
                val zipEntry = ZipEntry(outputFile.toPath().fileName.toString())
                zos.putNextEntry(zipEntry)
                zos.write(bis.readBytes())
                zos.closeEntry()
            }
        }
    }


    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {

        val inputFile = File("io/logfile.zip")
        val outputFile = File("io/unzippedLogfile.log")

        BufferedOutputStream(FileOutputStream(outputFile)).use { bos ->
            ZipInputStream(BufferedInputStream(FileInputStream(inputFile))).use { zis ->
                if (zis.available() == 1) {
                    zis.nextEntry
                    bos.write(zis.readBytes())
                }
            }
        }
    }
}
