import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import ru.sber.nio.Grep
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readLines
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val POST_SUBSTRING = "POST"
private const val POST_STRING_NUM = 6

class GrepTests {

    @Test
    fun `test grepping POST requests`() {
        val grep = Grep(Path("src", "test", "resources", "nio"))
        val resultFile = Path("src", "test", "resources", "nio", "result.txt")

        grep.find(POST_SUBSTRING)

        val resultLines = resultFile.readLines()
        assertAll(
            { assertEquals(POST_STRING_NUM, resultLines.size) },
            { assertTrue(resultLines.all { it.contains(POST_SUBSTRING) }) }
        )

        resultFile.deleteIfExists()
    }
}
