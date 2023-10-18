package ru.sber.nio

import ru.sber.exception.GrepClassException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

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
        val logsPath = Paths.get("io/logs")

        val resultFilePath = logsPath.resolve("result.txt")
        val foundLines: MutableList<String> = mutableListOf()

        try {
            Files.find(logsPath, 2, { filepath, attribute -> filepath.toString().endsWith(".log") })
                .forEach {
                    val fileContent = Files.lines(it).collect(Collectors.toList())
                    fileContent.forEachIndexed { index, line ->
                        val i = index + 1
                        if (line.contains(subString)) {
                            foundLines.add("${it.fileName} : $i : $line")
                        }
                    }
                }
        } catch (exception: Exception) {
            throw GrepClassException(exception.message)
        }

        Files.write(resultFilePath, foundLines)
    }
}