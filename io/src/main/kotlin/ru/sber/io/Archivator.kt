package ru.sber.io

import java.io.*
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
        val fileName = "io/logfile.log"
        val outputFileName = "io/logfile.zip"

        val zipOutputStream = ZipOutputStream(FileOutputStream(outputFileName))

        try {
            zipOutputStream.use { stream ->
                stream.putNextEntry(ZipEntry("logfile.log"))
                stream.write(File(fileName).readBytes())
                stream.closeEntry()
            }
        } catch (t: Throwable) {
            println(t.stackTrace)
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val inputArchivePath = "io/logfile.zip"
        val outputFilePath = "io/unzippedLogfile.log"

        try {
            ZipInputStream(FileInputStream(inputArchivePath)).use { zip ->
                val entry: ZipEntry? = zip.nextEntry

                if (entry?.name == "logfile.log") {
                    BufferedReader(InputStreamReader(zip)).useLines { lines ->
                        BufferedWriter(FileWriter(outputFilePath)).use { writer ->
                            lines.forEach { line -> writer.write(line + System.lineSeparator()) }
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            println(t.stackTrace)
        }
    }
}