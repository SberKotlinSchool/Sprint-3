package ru.sber.io

import java.io.File
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
    fun zipLogfile(pathNameSource: String = "io/logfile.log", pathNameTarget: String = "io/logfile.zip") {
        try {
            ZipOutputStream(FileOutputStream(pathNameTarget)).use { zipOutputStream ->
                FileInputStream(pathNameSource).use { fileInputStream ->
                    zipOutputStream.putNextEntry(ZipEntry(File(pathNameSource).name))
                    val buffer = ByteArray(fileInputStream.available())
                    fileInputStream.read(buffer)
                    zipOutputStream.write(buffer)
                    zipOutputStream.closeEntry()
                }
            }
        } catch (ex: Exception) {
            println(ex)
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */

    fun unzipLogfile(pathNameSource: String = "io/logfile.zip", pathNameTarget: String = "io/unzippedLogfile.log") =
        try {
            FileInputStream(pathNameSource).use { fileInputStream ->
                ZipInputStream(fileInputStream).use { zipInputStream ->
                    if (zipInputStream.nextEntry != null) {
                        FileOutputStream(pathNameTarget).use { fileOutputStream ->
                            var c = zipInputStream.read()
                            while (c != -1) {
                                fileOutputStream.write(c)
                                c = zipInputStream.read()
                            }
                            fileOutputStream.flush()
                            zipInputStream.closeEntry()
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            println(ex.message)
        }
}

fun main() {
    Archivator().zipLogfile()
    Archivator().unzipLogfile()
}