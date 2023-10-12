package ru.sber.io

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

internal class ArchivatorTest {

    @Test
    fun zipLogfile() {
        val archivator = Archivator()

        val fileName = "src/test/resources/logfile.log"
        val zipFileName = "src/test/resources/logfile.zip"

        assertFalse(File(zipFileName).exists())

        archivator.zipLogfile(fileName)
        //Проверка, что архив существует
        assertTrue(File(zipFileName).exists())
        assertEquals(464, Files.size(Path.of(zipFileName)))

        //Удаляем созданные файлы
        File(zipFileName).delete()
    }

    @Test
    fun unzipLogfile() {
        val archivator = Archivator()

        val fileName = "src/test/resources/logfile.log"
        val zipFileName = "src/test/resources/logfile.zip"
        val unzipFileName = "src/test/resources/unzippedLogfile.log"

        assertFalse(File(unzipFileName).exists())

        archivator.zipLogfile(fileName)
        assertTrue(File(zipFileName).exists())
        archivator.unzipLogfile(zipFileName, unzipFileName)

        //Проверяем, что исходный файл совпадает с разархивированным по содержимому
        assertTrue(File(unzipFileName).exists())
        assertTrue(isEqual(fileName, unzipFileName))

        //Удаляем созданные файлы
        File(zipFileName).delete()
        File(unzipFileName).delete()
    }

    private fun isEqual(firstFileName: String, secondFileName: String): Boolean {
        val firstFile = Path.of(firstFileName)
        val secondFile = Path.of(secondFileName)
        if (Files.size(firstFile) != Files.size(secondFile)) {
            return false
        }
        val first = Files.readAllBytes(firstFile)
        val second = Files.readAllBytes(secondFile)
        return first.contentEquals(second)
    }
}