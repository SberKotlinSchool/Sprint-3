package ru.sber.nio

import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {

    private val charset = Charsets.UTF_8
    private val decoder = charset.newDecoder()
    private val linePattern = Pattern.compile(".*\r?\n")
    private val outputFile = File("io", "result.txt")


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
        val file = File(outputFile.parent, "logs")
        val pattern = subString.toPattern()
        for (f in file.walk()) {
            if (f.isFile) {
                grep(f, pattern)
            }
        }
    }

    private fun grep(file: File, pattern: Pattern) {
        val fileName = file.name
        file.inputStream().use { inputStream ->
            inputStream.getChannel().use { channel ->
                val size = channel.size()
                val byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size)
                val charBuffer = decoder.decode(byteBuffer)
                val lm = linePattern.matcher(charBuffer)
                var pm: Matcher? = null
                var lineNumber = 0
                while (lm.find()) {
                    lineNumber++
                    val line = lm.group()
                    if (pm == null) pm = pattern.matcher(line) else pm.reset(line)
                    if (pm!!.find()) {
                        Files.writeString(
                            outputFile.toPath(),
                            "$fileName : $lineNumber : $line",
                            StandardOpenOption.APPEND
                        )
                    }
                    if (lm.end() == charBuffer.limit()) {
                        break
                    }
                }
            }
        }
    }
}