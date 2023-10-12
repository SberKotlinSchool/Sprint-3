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
     * Архив должен располагаться в том же каталоге, что и исходный файл.
     */
    fun zipLogfile(fileName: String) {

        val destinationFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip"

        ZipOutputStream(BufferedOutputStream(FileOutputStream(destinationFileName))).use { out ->
            FileInputStream(fileName).use { fileIS ->
                BufferedInputStream(fileIS).use { bufIS ->
                    val entry = ZipEntry(fileName.substring(fileName.lastIndexOf("/")))
                    out.putNextEntry(entry)
                    bufIS.copyTo(out, 1024)
                }
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохранить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(zipFileName: String, unzipFileName: String) {

        BufferedOutputStream(FileOutputStream(unzipFileName)).use { out ->
            FileInputStream(zipFileName).use { fileIS ->
                BufferedInputStream(fileIS).use { bufIS ->
                    ZipInputStream(bufIS).use { zipIS ->
                        zipIS.nextEntry
                        zipIS.copyTo(out, 1024)
                    }
                }
            }
        }
    }
}