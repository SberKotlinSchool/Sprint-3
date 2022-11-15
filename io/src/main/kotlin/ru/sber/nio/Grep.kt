package ru.sber.nio

import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.useLines


/**
 * Реализовать простой аналог утилиты grep с использованием классов из пакета java.nio.
 */
open class Grep {
    /**
     * Метод должен выполнить поиск![](../../../../../../../../../../../var/folders/pj/hwj43xhn57z593clmjnz21n00000gn/T/TemporaryItems/NSIRD_screencaptureui_QuHyUS/Screenshot 2022-11-09 at 12.50.14.png) подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */
    open fun find(subString: String) {
        val sourcePath = Paths.get("/Users/annapopova/ideaProjects/Sprint-3/io/logs")

        File("/Users/annapopova/ideaProjects/Sprint-3/io/result.txt").bufferedWriter().use { writer ->
            Files.walkFileTree(sourcePath, object : SimpleFileVisitor<Path>() {
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    file.useLines { it ->
                        var lineNumber = 1
                        it.forEach {
                            if (it.contains(subString)) {
                                var currResultList: MutableList<String> = mutableListOf<String>()
                                currResultList.add(file.fileName.toString())
                                currResultList.add(lineNumber.toString())
                                currResultList.add(it)
                                val result = currResultList.joinToString("  : ")
                                writer.appendLine(result)
                            } else lineNumber++
                        }
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }
}
