package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths
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
        val input = File("io/logfile.log")
        val inputPath = Paths.get(input.absolutePath).parent
        val output = File("$inputPath/logfile.zip")

        output
            .outputStream()
            .use { fos ->
                ZipOutputStream(fos).use { zos ->
                    zos.putNextEntry(ZipEntry(input.name))
                    FileInputStream(input).use { fis ->
                        val bytes = ByteArray(1024)
                        var length = fis.read(bytes)
                        while (length >= 0) {
                            zos.write(bytes, 0, length)
                            length = fis.read(bytes)
                        }
                    }
                }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val input = File("io/logfile.zip")
        val inputPath = Paths.get(input.absolutePath).parent
        val output = File("$inputPath/unzippedLogfile.log")

        input
            .inputStream()
            .use { fis ->
                ZipInputStream(fis).use { zis ->
                    zis.nextEntry
                    FileOutputStream(output).use { fos ->
                        val bytes = ByteArray(1024)
                        var length = zis.read(bytes)
                        while (length >= 0) {
                            fos.write(bytes, 0, length)
                            length = zis.read(bytes)
                        }
                    }
                }
            }
    }
}

fun main() {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}