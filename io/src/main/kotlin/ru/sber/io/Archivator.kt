package ru.sber.io

import java.io.*
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
open class Archivator {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    open fun zipLogfile(srcFile: File, zipFile: File) {
        FileInputStream(srcFile).use { fis ->
            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                zos.putNextEntry(ZipEntry(srcFile.name))
                var len: Int
                val buffer = ByteArray(1024)
                while (fis.read(buffer).also { len = it } > 0) {
                    zos.write(buffer, 0, len)
                }
                zos.closeEntry()
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохранить его в том же каталоге с именем unzippedLogfile.log
     */

    fun unzipLogfile(zipFile: File, unzippedFile: File) {
        FileInputStream(zipFile).use { fis ->
            ZipInputStream(BufferedInputStream(fis)).use { zis ->
                while ((zis.nextEntry) != null) {
                    FileOutputStream(unzippedFile, false).use { fos ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        BufferedOutputStream(fos, buffer.size)
                        while (zis.read(buffer).also { len = it } > 0) {
                            fos.write(buffer, 0, len)
                        }
                    }
                }
            }
        }
    }
}

