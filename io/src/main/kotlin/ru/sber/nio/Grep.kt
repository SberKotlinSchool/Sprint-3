package ru.sber.nio

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.io.path.isRegularFile
import kotlin.io.path.readLines
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
        File("result.txt").bufferedWriter().use { out ->
            Files.walk(Paths.get("logs/"))
                .filter { file -> file.toString().endsWith(".log") }
                .forEach { file ->
                    file.useLines { lines ->
                        lines.withIndex().filter { (i, value) -> value.contains(subString, true) }
                            .map { (i, value) -> "${file.fileName} : ${i + 1} : $value\n" }.forEach { out.write(it) }
                    }
                }
        }
    }
}