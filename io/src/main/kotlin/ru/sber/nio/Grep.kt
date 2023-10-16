package ru.sber.nio

import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.FileVisitOption.FOLLOW_LINKS
import java.nio.file.attribute.BasicFileAttributes

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */

fun main() {
    Grep().find("22/Jan/2001:14:27:46")
}

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
        val outputFile = File("io/result.txt")

        Files.newBufferedWriter(outputFile.toPath(), Charsets.UTF_8).use { writer ->
            Files.walkFileTree(
                Paths.get("./io/logs"),
                setOf(FOLLOW_LINKS),
                Int.MAX_VALUE,
                createFileVisitor(subString, writer)
            )
        }
    }

    private fun createFileVisitor(subString: String, writer: java.io.BufferedWriter): FileVisitor<Path> {
        return object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                file?.toFile()?.useLines { lines ->
                    lines.forEachIndexed { index, line ->
                        if (line.contains(subString)) {
                            writer.write("${file.fileName} : $index : $line\n")
                        }
                    }
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                println("Failed to visit file: $file")
                return FileVisitResult.CONTINUE
            }
        }
    }
}