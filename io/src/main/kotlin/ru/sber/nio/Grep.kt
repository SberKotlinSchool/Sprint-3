package ru.sber.nio

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isHidden
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
     *
     * @param subString Искомая подстрока
     * @param searchPathStr Путь до директории с файлами для поиска
     * @param resultFileName Путь до файла, куда будут сохранены результаты поиска
     */
    fun find(subString: String, searchPathStr: String, resultFileName: String): File {
        val resultFile = File(resultFileName)
        val searchPath = Paths.get(searchPathStr)

        resultFile.bufferedWriter()
                .use { bufferedWriter ->
                    Files.walk(searchPath)
                            .filter { file -> file.isRegularFile() && !file.isHidden() }//Только файлы и не скрытые/системные маковские
                            .forEach { file ->
                                file.useLines { lines ->
                                    lines.withIndex()
                                            .filter { (_, line) -> line.contains(subString, true) }
                                            .map { (i, line) -> "${file.fileName} : ${i + 1} : $line\n" }
                                            .forEach { bufferedWriter.write(it) }
                                }
                            }
                }

        return resultFile
    }

}