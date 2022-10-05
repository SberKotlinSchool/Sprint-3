package ru.sber.nio

import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
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
    companion object {
        @JvmStatic
        fun find(subString: String) {
            val sbResult = StringBuilder()
            Files.find(File("../io/logs").toPath(), 2, { name, _ -> name.toString().endsWith(".log") })
                .forEach { file ->
                    file.useLines {
                        it.mapIndexed { index, s -> "${index + 1} : $s" }.filter { it.contains(subString) }
                            .map { "${file.fileName} : $it" }.forEach { sbResult.append("$it\n") }
                    }
                }

            File("../io/result.txt").outputStream().use { stream ->
                stream.channel.use { channel ->
                    channel.write(
                        ByteBuffer.wrap(
                            sbResult.toString().toByteArray(
                                StandardCharsets.UTF_8
                            )
                        )
                    )
                }
            }
        }
    }
}

