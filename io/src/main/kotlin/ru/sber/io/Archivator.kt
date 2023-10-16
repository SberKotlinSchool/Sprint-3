package ru.sber.io

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    private val log = File("io/logfile.log")
    private val zipFile = File("io/logfile.zip")
    private val unZipLog = File("io/unzippedLogfile.log")

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { out ->
            out.putNextEntry(ZipEntry(log.name))
            log.inputStream().use { input ->
                input.copyTo(out)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        unZipLog.outputStream().use { out ->
            val zip = ZipFile(zipFile)
            zip.getInputStream(zip.getEntry(log.name)).use { input ->
                out.write(input.readAllBytes())
            }
        }
    }
}