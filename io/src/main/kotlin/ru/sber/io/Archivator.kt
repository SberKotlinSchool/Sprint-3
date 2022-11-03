package ru.sber.io


import java.io.BufferedOutputStream
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

    private val dirPath: String = System.getProperty("user.dir") //Системный путь проекта

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */

    fun zipLogfile() {
        val pathIn = File("$dirPath/logfile.log")
        val pathOut = File("$dirPath/logfile.zip")
        val inputSt = FileInputStream(pathIn).readAllBytes()
        val outputSt = BufferedOutputStream(FileOutputStream(pathOut))

        ZipOutputStream(outputSt).use { zip ->
            val zipEntry = ZipEntry(pathIn.toPath().fileName.toString())
            zip.putNextEntry(zipEntry)
            zip.write(inputSt)
        }

    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */

    fun unzipLogfile() {
        val pathIn = File("$dirPath/logfile.zip")
        val pathOut = File("$dirPath/unzippedLogfile.log")
        val inputSt = FileInputStream(pathIn)
        val outputSt = BufferedOutputStream(FileOutputStream(pathOut))

        ZipInputStream(inputSt).use { zipIn ->
            zipIn.nextEntry
            outputSt.use { zipOut -> zipIn.copyTo(zipOut) }
        }
    }
}