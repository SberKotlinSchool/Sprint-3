package ru.sber.nio

import java.nio.file.Paths
import kotlin.io.path.deleteIfExists

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
        val target = logsPath.parent.resolve("result.txt")
        target.deleteIfExists()
        logsPath
            .toFile().walk()
            .filter {
                !it.isDirectory
            }
            .map { file ->
                file.useLines {
                    it.mapIndexed { i, str ->
                        i to str
                    }
                        .filter { (_, str) -> str.contains(subString) }
                        .map { (i, str) ->
                            "${file.name} : ${i + 1} : $str"
                        }
                        .toList()
                }
            }
            .flatten()
            .let {
                target.toFile().writeText(it.joinToString(separator = "\n"))
            }

    }
}

fun main() {
    Grep().find("54.22.12.8")
}