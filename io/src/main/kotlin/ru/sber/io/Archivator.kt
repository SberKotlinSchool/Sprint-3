package ru.sber.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
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
    fun zipLogfile(file: File) {

        if (!file.exists()) {
            throw FileNotFoundException()
        }

        val zipFile = getNewFile(file.parent, file.nameWithoutExtension + ".zip")

        ZipOutputStream(zipFile.outputStream()).use { zos ->
            BufferedInputStream(file.inputStream()).use { bis ->
                val zipEntry = ZipEntry(file.name)
                zos.putNextEntry(zipEntry)
                bis.copyTo(zos, 1024)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(file: File) {

        ZipInputStream(file.inputStream()).use { zis ->
            val nextEntry = zis.nextEntry
            //в нашем случае только один файл
            if (nextEntry != null) {
                if (!nextEntry.isDirectory) {


                    val unZipFile = getNewFile(
                        file.parent,
                        "unzipped" + nextEntry.name.replaceFirstChar { it.uppercase() })

                    BufferedOutputStream(unZipFile.outputStream()).use { bos ->
                        zis.copyTo(bos, 1024)
                    }
                }
            }
        }
    }

    private fun getNewFile(folder: String?, newName: String): File {
        var newFileName = newName

        if (folder != null) {
            newFileName = folder + File.separator + newFileName
        }

        val zipFile = File(newFileName)
        return zipFile
    }
}