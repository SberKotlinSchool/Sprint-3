package ru.sber.nio

import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {
    /**
     * Метод должен выполнить поиск подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */

    private val buffer: ByteBuffer = ByteBuffer.allocate(1024)
    fun find(subString: String) {
        val resultPath = File("io/result.txt")
        val logsPath = Paths.get("io/logs")
        resultPath.outputStream().use { outputStream ->
            outputStream.channel.use { result ->
                Files.walk(logsPath)
                    .filter { p -> p.isRegularFile() }
                    .forEach { file ->
                        checkFile(file, subString, result)
                    }
            }
        }
    }

    private fun checkFile(file: Path, subString: String, result: FileChannel) =
        file.toFile().inputStream().bufferedReader().use { log ->
            log.readLines().forEachIndexed { index, line ->
                if (line.contains(subString)) {
                    writeResult(file.name, index, line, result)
                }
            }
        }


    private fun writeResult(
        filename: String, index: Int, line: String, result: FileChannel
    ) {
        val resultLine = "$filename : $index : $line\n"
        buffer.put(resultLine.toByteArray())

        buffer.flip()
        while (buffer.hasRemaining()) {
            result.write(buffer)
        }

        buffer.clear()
    }

}
