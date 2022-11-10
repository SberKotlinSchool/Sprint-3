package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path


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
        val sourcePath = Path("io/logs").toAbsolutePath()
        val targetPath = Path("io/result.txt").toAbsolutePath()
        Files.createFile(targetPath)
        Files.walk(sourcePath).filter { Files.isRegularFile(it) }.forEach { searchAndWrite(it, subString, targetPath) }
    }

    private fun searchAndWrite(sourcePath: Path, subString: String, targetPath: Path) {
        var count = 1
        val sb = StringBuilder()

        Files.readAllLines(sourcePath).forEach { string ->
            if (string.contains(subString)) {
                sb.append("${sourcePath.fileName} : $count : $string \n")
            }
            count++
        }
        if (sb.isNotEmpty()) {
            Files.write(targetPath, sb.lines(), StandardOpenOption.APPEND)
        }
    }
}