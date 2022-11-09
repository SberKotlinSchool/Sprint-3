package ru.sber.io

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {


    private companion object {
        const val BUFFER_SIZE = DEFAULT_BUFFER_SIZE
        const val LOG_FILE_NAME = "logfile.log"
        const val ZIPPED_FILE_NAME = "logfile.zip"
        const val UNZIPPED_FILE_NAME = "unzippedLogfile.log"
    }

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        (File(LOG_FILE_NAME) to File(ZIPPED_FILE_NAME))
            .let { (_if, _of) -> _if.inputStream() to ZipOutputStream(_of.outputStream()) }
            .let { (_fis, _fos) ->
                _fos.putNextEntry(ZipEntry(LOG_FILE_NAME))
                _fis.use {
                    _fos.use {
                        transfer(_fis, _fos)
                    }
                }
            }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        (File(ZIPPED_FILE_NAME) to File(UNZIPPED_FILE_NAME))
            .let { (_if, _of) -> ZipInputStream(_if.inputStream()) to _of.outputStream() }
            .let { (_fis, _fos) ->
                _fis.nextEntry
                transfer(_fis, _fos)
            }
    }


    /**
     * Функция чтения всех данных из потока _is и записи в поток _os
     */
    private fun transfer(_is: InputStream, _os: OutputStream) = _is.transferTo(_os)

    /**
     * Альтернативная функция чтения всех данных из потока _is и записи в поток _os
     */
    private fun transfer2(_is: InputStream, _os: OutputStream) {
        val buffer = ByteArray(BUFFER_SIZE)
        var s: Int
        do {
            s = _is.read(buffer)
            if (s > 0) _os.write(buffer, 0, s)
        } while (s > 0)
    }


}

fun main() {

    Archivator().zipLogfile()
    Archivator().unzipLogfile()

}