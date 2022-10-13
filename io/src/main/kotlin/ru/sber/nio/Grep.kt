package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.writeText
import kotlin.streams.toList

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {

    val RESULT_NAME = "./io/result.txt"
    val ROOT_PATH = "./io/logs"
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
        Files.walk(Path(ROOT_PATH)).use {
            val outputFile = Path(RESULT_NAME)
            val outputList = mutableListOf<String>()

            if (!outputFile.exists()) outputFile.createFile()

            it.filter { path -> path.isRegularFile() }
                .forEach { file ->
                    val lines = Files.lines(file).toList()
                    for (i in lines.indices) {
                        if (lines[i].contains(subString)) outputList.add(getLine(file.fileName, lines[i], i + 1))
                    }
                }
            outputFile.writeText(outputList.joinToString("\n"))
        }
    }

    private fun getLine(file: Path, line: String, i: Int): String {
        return "${file.fileName} : $i : $line"
    }
}