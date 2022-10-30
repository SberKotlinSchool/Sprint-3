package ru.sber.io

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.io.path.Path
import kotlin.io.path.absolute


/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archiver {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val source = Path("io/logfile.log").normalize().absolute()
        val target = Path("io/logfile.zip").normalize().absolute()

        target.toFile().outputStream().use { fout ->
            ZipOutputStream(fout).use { zout ->
                zout.putNextEntry(ZipEntry(source.fileName.toString()))
                source.toFile().inputStream().use {fin ->
                    val buffer = ByteArray(fin.available())
                    fin.read(buffer)
                    zout.write(buffer)
                    zout.closeEntry()
                }
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val source = Path("io/logfile.zip").normalize().absolute()
        val target = Path("io/unzippedLogfile.log").normalize().absolute()

        source.toFile().inputStream().use { fin ->
            ZipInputStream(fin).use { zin ->
                if (zin.available() == 1) {
                    zin.nextEntry
                    target.toFile().outputStream().use { fout ->
                        var len: Int
                        val buffer = ByteArray(1024)
                        while (zin.read(buffer).also { len = it } > 0) {
                            fout.write(buffer, 0, len)
                        }
                    }
                }
            }
        }
    }
}