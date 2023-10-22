package ru.sber.io

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
    companion object {
        private const val INPUT_FILE_NAME = "./io/logfile.log"
        private const val ARCHIVED_FILE_NAME = "./io/output.zip"
        private const val UNARCHIVED_PREFIX = "unarchived_"
        private const val UNARCHIVED_FILES_BASE_PATH = "./io/"
    }


    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        ZipOutputStream(FileOutputStream(ARCHIVED_FILE_NAME)).use { outputStream ->
            outputStream.putNextEntry(ZipEntry(INPUT_FILE_NAME))
            outputStream.write(FileInputStream(INPUT_FILE_NAME).use { inputStream ->
                inputStream.readBytes()
            })
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(FileInputStream(ARCHIVED_FILE_NAME)).use { inputStream ->
            var entry: ZipEntry? = inputStream.nextEntry
            while (entry != null) {
                FileOutputStream(
                    UNARCHIVED_FILES_BASE_PATH + UNARCHIVED_PREFIX + entry.name.split("/").last()
                ).use { outputStream ->
                    outputStream.write(inputStream.readBytes())
                }
                entry = inputStream.nextEntry
            }
        }
    }
}

fun main() {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}