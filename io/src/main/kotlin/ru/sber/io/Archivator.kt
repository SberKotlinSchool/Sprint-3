package ru.sber.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */

fun main(){
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}
class Archivator {
    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {

        File("io/logfile.log").inputStream().use{
            input ->
            ZipOutputStream(File("io/logfile.zip").outputStream()).use {
                output ->
                output.putNextEntry(ZipEntry("logfile.log"))
                input.copyTo(output)
            }
        }
    }
    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {

        ZipInputStream(File("io/logfile.zip").inputStream()).use{
            input ->
            input.nextEntry
            File("io/unzippedLogfile.log").outputStream().use {
                input.copyTo(it)
            }
        }
    }
}