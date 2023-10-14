package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isDirectory
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
        val resultFile = Paths.get("io/result.txt").toFile()
        val logPath = Paths.get("io/logs")
        if (resultFile.exists())
            resultFile.delete()
        Files.walk(logPath).filter { !it.isDirectory() }.forEach { file ->
            file.useLines { line ->
                line.forEachIndexed { index, string ->
                    if (string.contains(subString)) {
                        resultFile.appendText("${file.fileName} : $index : $string\n")
                    }
                }
            }
        }
    }
}

fun main() {
    Grep().find("54.22.12.8")
}