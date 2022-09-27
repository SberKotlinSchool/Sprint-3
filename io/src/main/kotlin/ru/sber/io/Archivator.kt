package ru.sber.io

import java.io.*
import java.util.zip.*
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
        val input = FileInputStream(BASICFILE)
        val entry = ZipEntry(FILENAME)
        val output = ZipOutputStream(FileOutputStream(ZIPPED))
        output.putNextEntry(entry)
        val buffer = ByteArray(1024)
        while (true) {
            var len = input.read(buffer)
            if (len < 0)
                break
            output.write(buffer, 0, len)
        }
        output.closeEntry()
        output.close()
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val input = ZipInputStream(FileInputStream(ZIPPED))
        val output = FileOutputStream(UNZIPPED)
        val buffer = ByteArray(1024)
        input.nextEntry
        while (true) {
            var len = input.read(buffer)
            if (len < 0)
                break
            output.write(buffer, 0, len);
        }
        input.close()
        output.close()
    }

    companion object {
        val FILENAME = "logfile.log"
        val BASICFILE = "io/logfile.log"
        val ZIPPED = "io/logfile.zip"
        val UNZIPPED = "io/unzippedLogfile.log"
    }
}