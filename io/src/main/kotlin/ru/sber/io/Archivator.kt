package ru.sber.io

import java.io.File
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {
    private val bufferSize = 1024

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val input = File("io/logfile.log")
        val output = File("io/logfile.zip")
        ZipOutputStream(output.outputStream().buffered(bufferSize))
            .use { outputStream ->
                outputStream.putNextEntry(ZipEntry(input.name))
                input.inputStream().buffered(bufferSize)
                    .use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val input = File("io/logfile.zip")
        val output = File("unzippedLogfile.log")
        output.outputStream().buffered(bufferSize)
            .use { outputStream ->
                ZipInputStream(input.inputStream().buffered(bufferSize))
                    .use { inputStream ->
                        inputStream.nextEntry
                        inputStream.copyTo(outputStream)
                    }
            }
    }
}

fun main() {
    val archivator = Archivator()
    val input = File("io/logfile.log")
    val output = File("unzippedLogfile.log")
    archivator.zipLogfile()
    archivator.unzipLogfile()
    print("После упаковки и распаковки содержимое файла не изменилось: " + input.isFileContentEquals(output))
}

private fun File.isFileContentEquals(anotherFile: File): Boolean {
    val first = Files.readAllBytes(this.toPath())
    val second = Files.readAllBytes(anotherFile.toPath())
    return first.contentEquals(second)
}