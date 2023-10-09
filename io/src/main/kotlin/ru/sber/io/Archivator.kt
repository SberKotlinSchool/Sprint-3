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
    private val fileName = "logfile.log"
    private val zipFileName = "logfile.zip"
    private val parent = "io"

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        try {
            ZipOutputStream(FileOutputStream("$parent/$zipFileName")).use { zipOut ->
                FileInputStream("$parent/$fileName").use { fis ->
                    val entry = ZipEntry(fileName)
                    zipOut.putNextEntry(entry)
                    val buffer = ByteArray(fis.available())
                    fis.read(buffer)
                    zipOut.write(buffer)
                    zipOut.closeEntry()
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        try {
            ZipInputStream(FileInputStream("$parent/$zipFileName")).use { zipIn ->
                while (zipIn.getNextEntry() != null) {
                    val fileOut = FileOutputStream("$parent/unzipped$fileName")
                    var data = zipIn.read()
                    while (data != -1) {
                        fileOut.write(data)
                        data = zipIn.read()
                    }
                    fileOut.flush()
                    zipIn.closeEntry()
                    fileOut.close()
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
    }
}