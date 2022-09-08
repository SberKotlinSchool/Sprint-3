package ru.sber.io

import java.io.File
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
     * Архив должен располагаться в том же каталоге, что и исходный файл.
     */
    fun zipLogfile(sourceFileName: String = "logfile.log", zipFileName: String = "logfile.zip", path: String = "io") {
        File("$path${File.separator}$sourceFileName").inputStream().use { fis ->
            ZipOutputStream(FileOutputStream("$path${File.separator}$zipFileName")).use {
                val zipEntry = ZipEntry(sourceFileName)
                it.putNextEntry(zipEntry)

                val buffer = ByteArray(2048)

                var result: Int
                while (fis.read(buffer).also { length -> result = length } > -1) {
                    it.write(buffer, 0, result)
                }
                it.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(
        sourceZipFile: String = "logfile.zip",
        destFileName: String = "unzippedLogfile.log",
        path: String = "io"
    ) {
        ZipInputStream(File("$path${File.separator}$sourceZipFile").inputStream()).use { zis ->
            val buffer = ByteArray(2048)
            var fileName = destFileName
            var counter = 0

            //Если файлов в архиве несколько
            var entry: ZipEntry?
            while (zis.nextEntry.also { e -> entry = e } != null) {

                FileOutputStream("$path${File.separator}$fileName").use {
                    var result: Int
                    while (zis.read(buffer).also { length -> result = length } > -1) {
                        it.write(buffer, 0, result)
                    }
                    it.flush()
                    zis.closeEntry()
                }
                fileName += "-${counter++}"
            }
        }
    }
}