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
        ZipOutputStream(BufferedOutputStream(FileOutputStream("src/main/resources/file/logfile.zip"))).use { output ->
            BufferedInputStream(FileInputStream("src/main/resources/file/logfile.log")).use { input ->
                val entry = ZipEntry("logfile.log")
                output.putNextEntry(entry)
                output.write(input.readAllBytes())
                output.closeEntry()
            }
        }
    }


    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        BufferedOutputStream(FileOutputStream("src/main/resources/file/unzippedLogfile.log")).use { output ->
            ZipInputStream(BufferedInputStream(FileInputStream("src/main/resources/file/logfile.zip"))).use { input ->
                input.nextEntry
                output.write(input.readAllBytes())
                input.closeEntry()
            }
        }
    }
}