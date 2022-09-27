package ru.sber.nio

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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

    val resultFileName: String = "io/result.txt"
    val logPath: String = "io/logs"

    /**
     * Метод выполняет поиск искомой строки
     * в во всех файлах по указаному пути + поддиректории
     */
    fun find(subString: String) {

        val logPath = Paths.get(logPath)

        deleteResultFile()
        Files.walk(logPath).forEach { file ->
            if (file.isRegularFile()) {
                val findRecords = getStrFromFile(file, subString)
                if (findRecords.isNotEmpty()) {
                    saveDataToFile(findRecords)
                }
            }
        }
        println("Searching success, results can be found in $resultFileName")
    }


    /**
     * Метод для поиска строки в указанном файле.
     * Если строка найдена, добавляет запись в mutableList
     * в формате: имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     */
    private fun getStrFromFile(file: Path, subString: String): List<String> {
        val resultList: MutableList<String> = mutableListOf()

        file.useLines { fileContent ->

            fileContent.forEachIndexed() { strIndex, fileStr ->
                if (fileStr.contains(subString)) {
                    resultList.add("${file.name} : ${strIndex + 1} : ${fileStr}")
                }
            }
            return resultList.toList()
        }
    }

    /**
     * Метод записывает результаты поиска в файл
     */
    private fun saveDataToFile(data: List<String>) {
        try {
            data.forEach { str ->
                File(resultFileName).appendText(str + "\n")
            }
        } catch (e: Exception) {
            println("Some errors while writing result ${e.message}")
        }

    }
    /**
     * Метод удаляет результаты предыдушего поиска
     */
    private fun deleteResultFile() {
        val resultFile = File(resultFileName)
        try {
            if (resultFile.exists()) {
                resultFile.delete()
            }
        } catch (e: IOException) {
            println("Error while deleting previous results")
        }

    }


}

//testDrive
//fun main() {
//    val gr = Grep()
//    gr.find("22/Jan/2001:14:27:46")
//}