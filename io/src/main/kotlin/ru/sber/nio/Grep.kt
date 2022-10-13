package ru.sber.nio

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.*
import kotlin.streams.toList

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
        val directory = "./io/logs"
        val result = "./io/result.txt"

        if(!Paths.get(result).exists()) {
            Files.createFile(Paths.get(result))
        }

        val resultFile = Paths.get(result)
        resultFile.writeText("")
        val listPath = Files.walk(Paths.get(directory)).toList()
        for(path in listPath){
            if(path.isRegularFile()){
             //   for(line in path.readLines()){
                for(i in 0..path.readLines().size-1) {
                    if(path.readLines()[i].contains(subString))
                        resultFile.appendText(path.fileName.toString() + " : " + (i+1) +" : "+ path.readLines()[i] + "\n")
                }
            }
        }
    }
}

fun main(){
    val grep = Grep()
    grep.find("22/Jan/2001:14:27:46")
}