package ru.sber.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
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
        val outputFileName = "io/logfile.zip"
        val inputFileName = "io/logfile.log"
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFileName))).use { zip ->
            BufferedInputStream(FileInputStream(inputFileName)).use { inputFile ->
                val entry = ZipEntry(inputFileName.substring(inputFileName.lastIndexOf("/") + 1))
                zip.putNextEntry(entry)
                inputFile.copyTo(zip)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFileName = "io/logfile.zip"
        val zipTargetFileName = "logfile.log"
        val unzippedFileName = "io/unzippedLogfile.log"

        val zipFile = ZipFile(File(zipFileName))

        ZipInputStream(BufferedInputStream(FileInputStream(zipFileName))).use { zip ->
            var entry: ZipEntry? = zip.nextEntry
            while (entry != null) {
                if (entry.name.equals(zipTargetFileName)) {
                    BufferedOutputStream(FileOutputStream(unzippedFileName)).use { out ->
                        entry?.let { zipFile.getInputStream(it).use { content ->
                            content.copyTo(out)
                        } }
                    }
                }
                entry = zip.nextEntry
            }
        }
    }
}