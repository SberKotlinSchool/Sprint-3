package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.appendLines
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.useLines

private const val DEFAULT_DIRECTORY = "io"
private const val DEFAULT_SEARCH_DIRECTORY = "logs"

private const val RESULT_FILENAME = "result.txt"

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep(private val directory: Path = Path(DEFAULT_DIRECTORY)) {
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
    fun find(subString: String) {
        val resultFile = Path(directory.pathString, RESULT_FILENAME).toAbsolutePath().apply {
            deleteIfExists()
            createFile()
        }

        Files.walk(Path(directory.pathString, DEFAULT_SEARCH_DIRECTORY))
            .filter { it.isRegularFile() }
            .forEach { path ->
                val resultLines = path.useLines { lines ->
                    lines.mapIndexedNotNull { lineIndex, line ->
                        if (line.contains(subString)) {
                            "${path.name} : ${lineIndex + 1} : $line"
                        } else {
                            null
                        }
                    }.toList()
                }

                resultFile.appendLines(resultLines)
            }
    }
}
