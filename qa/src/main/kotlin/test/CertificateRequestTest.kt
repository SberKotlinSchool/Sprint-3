package test

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import ru.sber.qa.*
import kotlin.random.Random

internal class CertificateRequestTest {
    private val certificateType = mockkClass(CertificateType::class)

    @BeforeEach
    fun setUp() {
        mockkObject(Random)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun processDataCallScannerGetScanData() {
        //given
        every { Random.nextLong(5000L, 15000L) } returns Random.nextLong(10001L, 15000L)
        // then
        assertThrows(ScanTimeoutException::class.java) { CertificateRequest(1L,certificateType).process(2L).data }
    }
}