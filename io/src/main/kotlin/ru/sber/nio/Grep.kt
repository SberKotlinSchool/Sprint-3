package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream
import kotlin.io.path.isRegularFile
import kotlin.io.path.isSameFileAs

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
        val path = Paths.get("logs")
        val destFile = Paths.get("logs/result.txt")

        Files.newOutputStream(destFile).use { dest ->
            Files.walk(path)
                .filter  { it.isRegularFile() && !it.isSameFileAs(destFile) }
                .flatMap { getLinesStream(it, subString) }
                .forEach { dest.write(it.toByteArray())}
        }
    }

    fun getLinesStream(path : Path, subString : String) : Stream<String> {
        val i = AtomicInteger(0);
        return Files.lines(path)
            .map    { "${path.fileName} : ${i.incrementAndGet()} : $it\n"}
            .filter { subString in it }
    }

}

fun main() {
    Grep().find("22/Jan/2001:14:27:46")
}