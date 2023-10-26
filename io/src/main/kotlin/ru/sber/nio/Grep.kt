package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

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
    fun find(subString: String) {
        val resultPath = Paths.get("io/result.txt")
        if (Files.exists(resultPath)) {
            Files.delete(resultPath)
        }
        Files.createFile(resultPath)

        Files.walk(Paths.get("io/logs"))
                .filter(Files::isRegularFile)
                .forEach { file ->
                    Files.readAllLines(file)
                            .mapIndexed { index, line -> LineDto(line, index + 1) }
                            .filter { line -> line.content.contains(subString) }
                            .map { line -> "${file.fileName} : ${line.lineNumber} : ${line.content}" }
                            .forEach { line -> Files.write(resultPath, listOf(line), StandardOpenOption.APPEND) }
                }
    }

    inner class LineDto(val content: String, val lineNumber: Int)
}
