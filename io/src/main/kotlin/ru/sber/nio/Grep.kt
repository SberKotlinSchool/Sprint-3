package ru.sber.nio

import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
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

        val logPath = Paths.get("io/logs")
        Files.walk(logPath).forEach { file ->
            if (file.isRegularFile()) {
                val findRecords = getStrFromFile(file, subString)
                //println("${file.name} === $findRecords")
                if (findRecords.isNotEmpty()) {
                    saveDataToFile(findRecords)
                    println(file.parent)
                }
            }
        }
    }

    private fun getStrFromFile(file: Path, subString: String): List<String> {
        return file.useLines { fileStr ->
            fileStr.filter { str ->
                str.contains(subString)
            }.toList()
        }
    }

    private fun saveDataToFile(data: List<String>) {
        FileWriter("result").use { fw ->
            data.forEach { str ->
                //fw.write("$str/n")
                File("result111").appendText(str + "\n")
            }
        }
    }
}

fun main() {
    val gr = Grep()
    gr.find("127.0.0.1")
}