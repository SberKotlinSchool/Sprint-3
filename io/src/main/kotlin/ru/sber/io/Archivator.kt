package ru.sber.io

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

private const val DEFAULT_DIRECTORY = "io"

private const val FILENAME_LOG = "logfile.log"
private const val FILENAME_ZIP = "logfile.zip"

private const val BUFFER_SIZE = 1_024

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator(private val directory: File = File(DEFAULT_DIRECTORY)) {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        File(directory, FILENAME_LOG).inputStream().use { input ->
            ZipOutputStream(File(directory, FILENAME_ZIP).outputStream()).use { output ->
                output.putNextEntry(ZipEntry(FILENAME_LOG))
                input.copyTo(output, BUFFER_SIZE)
                output.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        ZipInputStream(File(directory, FILENAME_ZIP).inputStream()).use { input ->
            var entry: ZipEntry?
            do {
                entry = input.nextEntry

                if (entry != null && input.available() != 0) {
                    File(directory, entry.name).outputStream().use { output ->
                        input.copyTo(output, BUFFER_SIZE)
                    }
                }

            } while (entry != null)
        }
    }
}
