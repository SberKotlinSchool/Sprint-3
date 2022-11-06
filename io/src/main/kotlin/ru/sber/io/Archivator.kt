package ru.sber.io

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.charset.Charset
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
    fun zipLogfile(fileName: String = "logfile.log", directory: String = "io/", fileNameZip: String = fileName.substring(0,fileName.lastIndexOf('.'))) {
        lateinit var zipOutputStream: ZipOutputStream
        try{
            val file = File("$directory$fileName")
            val data = file.inputStream().buffered().readBytes()
            zipOutputStream = ZipOutputStream(FileOutputStream("$directory$fileNameZip.zip"))
            zipOutputStream.use {
                zipOutputStream.putNextEntry(ZipEntry(fileName))
                zipOutputStream.write(data)
                zipOutputStream.closeEntry()
            }
        }catch (e:Exception){
            print(e.message)
        }finally {
            zipOutputStream.close()
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile(fileNameZip: String = "logfile", directory: String = "io/", fileName: String = "unzipped${fileNameZip[0].uppercase()}${fileNameZip.substring(1)}") {
       lateinit var zipInputStream: ZipInputStream
        try{
            zipInputStream = ZipInputStream(FileInputStream("$directory$fileNameZip.zip"))
            val nextEntry = zipInputStream.nextEntry
            val fileNameInZip = nextEntry?.name
            val fileFormat = fileNameInZip?.substring(fileNameInZip.lastIndexOf('.'))
            val file = File("$directory$fileName$fileFormat")
            while( nextEntry != null) {
                val lines = zipInputStream.bufferedReader(Charset.forName("UTF-8")).readLines()
                val writer = file.writer(Charset.forName("UTF-8"))
                writer.use { w -> lines.forEach { line -> w.write(line + "\n") } }
                zipInputStream.closeEntry()
            }
        }catch (e:Exception){
            print(e.message)
        }finally {
            zipInputStream.close()
        }
    }

}
