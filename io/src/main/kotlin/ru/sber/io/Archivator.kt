package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {


    private val logFile = File("io/logfile.log ")
    private val archivedLogFile: File = File("io/logfile.zip")
    private val unzippedLogfile = File("io/unzippedLogfile.log")
    private val readBuffer = ByteArray(1024)

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {

        try {
            println("Start creating archive for ${logFile.path}")

            ZipOutputStream(FileOutputStream(archivedLogFile)).use { zos ->
                zos.putNextEntry(ZipEntry(logFile.name))
                FileInputStream(logFile).use { fis ->

                    do {
                        fis.read(readBuffer)
                        zos.write(readBuffer)
                    } while (fis.read() != -1)
                }
            }
            println("Archive ${archivedLogFile.name} create successful.")

        } catch (e: IOException) {
            println("Error while creating archive ${e.message}")
        }

    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {


        println("Start unarchive ${archivedLogFile.path}")

        try {
            ZipInputStream(FileInputStream(archivedLogFile)).use { zis ->
                while (zis.nextEntry != null) {
                    Files.copy(zis, unzippedLogfile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    zis.nextEntry
                }
                zis.closeEntry()
                println("Success unarchive ${archivedLogFile.name} to ${unzippedLogfile.path}")
            }
        } catch (e: IOException) {
            println("Error while unzip $archivedLogFile ${e.message}")
        }

    }
}
//testDrive
//fun main() {
//    val arch = Archivator()
//    arch.zipLogfile()
//    println("================================")
//    arch.unzipLogfile()
//}