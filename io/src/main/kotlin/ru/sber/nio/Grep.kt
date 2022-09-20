package ru.sber.nio

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributeView
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
        val projectPath = System.getProperty("user.dir")
        val path = Paths.get(projectPath, "logs")

        val resultFileList = mutableListOf<String>()
        Files.walk(path)
            .filter {
                isFile(it)
            }
            .forEach { file ->
                File(file.toUri()).useLines { lines ->
                    findSubstringInFile(lines, subString, resultFileList, file)
                }
            }
        val resultPath = Paths.get(projectPath, "result.txt")
        File(resultPath.toUri()).writeText(resultFileList.stream().collect(Collectors.joining("\n")))

    }

    private fun findSubstringInFile(
        lines: Sequence<String>,
        subString: String,
        resultFileList: MutableList<String>,
        file: Path
    ) {
        lines.iterator()
            .withIndex()
            .forEach {
                if (it.value.contains(subString)) {
                    resultFileList.add(
                        file.fileName.toString() + " : "
                                + (it.index + 1) + " : " + it.value
                    )
                }
            }
    }

    private fun isFile(it: Path?) =
        Files.getFileAttributeView(it, BasicFileAttributeView::class.java).readAttributes().isRegularFile
}