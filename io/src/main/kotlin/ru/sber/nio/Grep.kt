package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.appendText
import kotlin.io.path.name
import java.nio.file.FileAlreadyExistsException

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */

class Grep {

    private val dirPath: String = System.getProperty("user.dir") //Системный путь проекта

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
        val pathDir: Path = Paths.get("$dirPath/logs")
        val resultPath: Path = Paths.get("$dirPath/result.txt")
        val resultTxt = try {
            Files.createFile(resultPath)
        } catch (ex: FileAlreadyExistsException) {
            Files.delete(resultPath)
            Files.createFile(resultPath)
        }
        val listFiles = Files.walk(pathDir).filter { it.toString().endsWith(".log") }.toList()
        for (f in listFiles) {
            val thisList = Files.lines(f).toList()
            val nameFiles = f.name
            for (text in thisList.filter { it.uppercase().contains(subString.uppercase()) }) {
                //Начинаем не с позиции 0, а с 1й
                val indexLine: String = (thisList.indexOf(text) + 1).toString()
                val bestLine = "$nameFiles : $indexLine : $text \n"
                resultTxt.appendText(bestLine)
            }
        }
    }
}