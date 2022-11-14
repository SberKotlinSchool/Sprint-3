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

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val file = FileInputStream("logfile.log")
        val zipFile = FileOutputStream("logfile.zip")
        val zip = ZipOutputStream(zipFile)
        file.use { fi ->
            zip.use { out ->
                val ze = ZipEntry("logfile.log")
                out.putNextEntry(ze)
                var b = fi.read()
                while (b != -1) {
                    out.write(b)
                    b = fi.read()
                }
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val zipFile = FileInputStream("logfile.zip")
        val zip = ZipInputStream(zipFile)
        val file = FileOutputStream("unzippedLogfile.log")
        zip.use { out ->
            file.use { fo ->
                out.nextEntry.let {
                    var b = out.read()
                    while (b != -1) {
                        fo.write(b)
                        b = out.read()
                    }
                }
            }
        }
    }
}
