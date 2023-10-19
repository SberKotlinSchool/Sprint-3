package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.useLines

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
        val pathFolder = Paths.get("io/logs")
        val resultFileName = Path.of("io/result.txt")
        try {
            Files.newBufferedWriter(resultFileName).use { result ->
                Files.walk(pathFolder).use { stream ->
                    stream
                        .filter(Path::isRegularFile)
                        .forEach { path ->
                            path.useLines { line ->
                                line.withIndex()
                                    .filter { it.value.contains(subString) }
                                    .forEach { indexedLine ->
                                        result.appendLine(
                                            "${path.fileName} : ${indexedLine.index + 1} : ${indexedLine.value}}"
                                        )
                                    }
                            }
                        }
                }
            }
            println("Выполнена работа grep с подстрокой $subString, сформирован файл $resultFileName")
        } catch (ex: Exception) {
            println("Ошибка работы grep с подстрокой $subString")
            throw ex
        }
    }
}