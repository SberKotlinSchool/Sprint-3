package ru.sber.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
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

    companion object {
        const val BUFFER_SIZE = 1024
    }

    fun zipLogfile() {
        val source = "io/logfile.log"
        val target = "io/logfile.zip"

        ZipOutputStream(BufferedOutputStream(FileOutputStream(target))).use { out ->
            FileInputStream(source).use { fis ->
                BufferedInputStream(fis).use { bis ->
                    val entry = ZipEntry(source.substring(source.lastIndexOf("/") + 1))
                    out.putNextEntry(entry)
                    bis.copyTo(out, BUFFER_SIZE)
                }
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val source = "io/logfile.zip"
        val target = "io/unzippedLogfile.log"

        ZipInputStream(BufferedInputStream(FileInputStream(source))).use { fis ->
            while (fis.nextEntry != null) {
                FileOutputStream(target).use { fos ->
                    fis.copyTo(fos, BUFFER_SIZE)
                }
            }
        }
    }
}

fun main() {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}