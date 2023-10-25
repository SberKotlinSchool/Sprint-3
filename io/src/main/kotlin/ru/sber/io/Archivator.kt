package ru.sber.io

import ru.sber.exception.UnzipFileException
import ru.sber.exception.ZipFileException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    private val LOG_EXTENSION: String = ".log"
    private val ZIP_EXTENSION: String = ".zip"

    var filepath: String = ""


    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        try {
            val inputFile = FileInputStream(filepath)
            val outputFile = FileOutputStream(changeFileExtensionTo(filepath, ZIP_EXTENSION))
            val zip: ZipOutputStream = ZipOutputStream(outputFile)

            zip.use {
                zip.putNextEntry(ZipEntry(File(filepath).name))
                val buffer = ByteArray(inputFile.available())
                inputFile.read(buffer)
                zip.write(buffer)
                zip.closeEntry()
            }
        } catch (exception: FileNotFoundException) {
            throw ZipFileException("File not found")
        } catch (exception: Exception) {
            throw ZipFileException("Unknown exception")
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        try {
            val inputFile = FileInputStream(filepath)
            val outputFile = FileOutputStream(getDirectory(filepath) + "unzippedL" + changeFileExtensionTo(getFilename(filepath), LOG_EXTENSION))
            val zip: ZipInputStream = ZipInputStream(inputFile)

            zip.use { zipIn ->
                while (zipIn.getNextEntry() != null) {
                    var data = zipIn.read()
                    while (data != -1) {
                        outputFile.write(data)
                        data = zipIn.read()
                    }
                    outputFile.flush()
                    zipIn.closeEntry()
                    outputFile.close()
                }
            }
        } catch (exception: FileNotFoundException) {
            throw UnzipFileException("File not found")
        } catch (exception: Exception) {
            throw UnzipFileException("Unknown exception")
        }
    }

    private fun changeFileExtensionTo(filepath: String, extension: String): String {
        var filename = filepath.substringBeforeLast('.', "")
        filename += extension
        return filename
    }

    private fun getDirectory(filepath: String): String {
        return filepath.substringBeforeLast('/', "") + "/"
    }

    private fun getFilename(filepath: String): String {
        return filepath.substringAfter("/l", "")
    }
}