package ru.sber.io

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
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
        val zipFile = File("io/logfile.zip")
        val file = File("io/logfile.log")
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
                if (file.length() > 1) {
                    FileInputStream(file).use { input ->
                        BufferedInputStream(input).use { origin ->
                            output.putNextEntry(ZipEntry(file.name))
                            origin.copyTo(output, 1024)
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
        val zipFile = File("io/logfile.zip")
        val file = File("io/unzippedLogfile.log")

        ZipFile(zipFile).use { zf ->
            for (e in zf.entries()){
                if(e.name == "logfile.log")
                {
                    InputStreamReader(zf.getInputStream(zf.getEntry(e.name))).use {out->
                        file.writeText(out.readText())
                    }
                }
            }
        }
    }
}