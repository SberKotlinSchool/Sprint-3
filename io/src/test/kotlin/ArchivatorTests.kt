import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import ru.sber.io.Archivator
import java.io.File
import java.util.zip.ZipInputStream
import kotlin.io.path.fileSize
import kotlin.io.path.readBytes
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val TEST_RESOURCES_DIRECTORY = "src/test/resources/io"

private const val ZIP_DIRECTORY = "zip"
private const val UNZIP_DIRECTORY = "unzip"

private const val ZIP_FILE_NAME = "logfile.zip"
private const val LOG_FILE_NAME = "logfile.log"

class ArchivatorTests {

    @Test
    fun `test zipping`() {
        val outputDir = File(TEST_RESOURCES_DIRECTORY, ZIP_DIRECTORY)
        val archivator = Archivator(outputDir)

        archivator.zipLogfile()

        val resultFile = outputDir.listFiles { file -> file.name == ZIP_FILE_NAME }?.singleOrNull()
        val properFile = File(TEST_RESOURCES_DIRECTORY, "$UNZIP_DIRECTORY/$ZIP_FILE_NAME")

        assertNotNull(resultFile)
        assertEquals(properFile.toPath().fileSize(), resultFile.toPath().fileSize())
        ZipInputStream(resultFile.inputStream()).use { resultZip ->
            ZipInputStream(properFile.inputStream()).use { properZip ->
                val properZipEntry = properZip.nextEntry
                val resultZipEntry = resultZip.nextEntry

                assertNotNull(properZipEntry)
                assertNotNull(resultZipEntry)
                assertAll(
                    { assertEquals(properZipEntry.name, resultZipEntry.name) },
                    { assertEquals(properZipEntry.compressedSize, resultZipEntry.compressedSize) },
                    { assertEquals(properZipEntry.crc, resultZipEntry.crc) },
                )
            }
        }

        resultFile.delete()
    }

    @Test
    fun `test unzipping`() {
        val outputDir = File(TEST_RESOURCES_DIRECTORY, UNZIP_DIRECTORY)
        val archivator = Archivator(outputDir)

        archivator.unzipLogfile()

        val resultFile = outputDir.listFiles { file -> file.name == LOG_FILE_NAME }?.singleOrNull()
        val properFile = File(TEST_RESOURCES_DIRECTORY, "$ZIP_DIRECTORY/$LOG_FILE_NAME")

        assertNotNull(resultFile)
        assertAll(
            { assertEquals(properFile.toPath().fileSize(), resultFile.toPath().fileSize()) },
            { assertContentEquals(properFile.toPath().readBytes(), resultFile.toPath().readBytes()) }
        )

        resultFile.delete()
    }
}
