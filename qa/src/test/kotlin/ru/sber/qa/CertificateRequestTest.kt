package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class CertificateRequestTest {

    @Test
     fun `process() should proceed correctly` () {
        // given
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
        val req = CertificateRequest(1L, CertificateType.NDFL)
        val employeeNum = 1L

        // when
        val cert = req.process(employeeNum)

        // then
        assertEquals(employeeNum, cert.processedBy)
        unmockkObject(Scanner)
     }


}