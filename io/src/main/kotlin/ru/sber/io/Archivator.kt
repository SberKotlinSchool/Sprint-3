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
    val logfileLog = "logfile.log"
    val logfileZip = "logfile.zip"
    val unzippedLogfileLog = "unzippedLogfile.log"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {


        ZipOutputStream(BufferedOutputStream(FileOutputStream(logfileZip))).use {output ->
            FileInputStream(File(logfileLog)).use { input ->
                val entry = ZipEntry(logfileZip)
                output.putNextEntry(entry)
                input.copyTo(output, 1024)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(BufferedInputStream(FileInputStream(logfileZip))).use { input ->
            FileOutputStream(File(unzippedLogfileLog)).use { output ->
                var entry = input.nextEntry
                while (entry != null) {
                    input.copyTo(output)
                    entry = input.nextEntry
                }
            }
        }
    }
}