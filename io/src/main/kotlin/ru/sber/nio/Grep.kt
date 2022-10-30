package ru.sber.nio

import java.nio.charset.StandardCharsets
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
        val source = Path("io/logs").toAbsolutePath().normalize()
        val target = Path("io/result.txt").toAbsolutePath().normalize()

        Files.deleteIfExists(target)

        Files.walk(source).filter { Files.isRegularFile(it) }
            .forEach { searchInFileAndWriteResult(it, subString, target) }
    }

    private fun searchInFileAndWriteResult(path: Path, subString: String, target: Path) {
        val stringBuilder = StringBuilder()
        var counter = 1

        Files.readAllLines(path, StandardCharsets.UTF_8).forEach {
            if (it.contains(subString)) {
                if (stringBuilder.isNotEmpty()) {
                    stringBuilder.append("\n")
                }
                stringBuilder.append("${path.fileName} : $counter : $it")
            }
            counter++
        }
        if (stringBuilder.isNotEmpty()) {
            if (!Files.isRegularFile(target)) {
                Files.createFile(target)
            }
            Files.write(target, stringBuilder.lines(), StandardOpenOption.APPEND)
        }
    }
}