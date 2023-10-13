package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.readLines

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {
    private val ioPrefix: String = "io"
    private val logsDir: String = "$ioPrefix/logs"
    private val resultFile = "$ioPrefix/result.txt"

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
        val resultFilePath: Path = Paths.get(resultFile)
        Files.deleteIfExists(resultFilePath)
        val resultFile = Files.createFile(resultFilePath)

        Files.walk(Paths.get(logsDir)).filter {
            Files.isRegularFile(it)
        }.forEach { file ->
            file.readLines().mapIndexed { index, s ->
                if (s.contains(subString))
                    resultFile.appendText("${file.fileName} : ${index + 1} : $s\n")
            }
        }
    }
}

fun main() {
    Grep().find("22/Jan/2001:14:27:46")
}