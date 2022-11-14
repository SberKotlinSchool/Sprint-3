package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

fun getClock(time: String): Clock =
    Clock.fixed(Instant.parse(time), ZoneId.of("UTC"))

class HrDepartmentTest {
    private val certificateRequest = mockk<CertificateRequest>()

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `receiveRequest Throws WeekendDayException`() {
        HrDepartment.clock = getClock("2022-10-30T10:15:30.00Z")

        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `first throw NotAllowReceiveRequestException`() {
        HrDepartment.clock = getClock("2022-10-25T10:15:30.00Z") //TUESDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `second throw NotAllowReceiveRequestException`() {
        HrDepartment.clock = getClock("2022-10-24T10:15:30.00Z") //MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `success push and poll`() {
        HrDepartment.clock = getClock("2022-10-24T10:15:30.00Z") //MONDAY
        val hrEmployeeNumber = 1L
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns ByteArray(1)
        val certificateR = CertificateRequest(hrEmployeeNumber, CertificateType.NDFL)
        HrDepartment.receiveRequest(certificateR)
        val incomeBox: LinkedList<*> = getField("incomeBox")
        val outcomeOutcome: LinkedList<*> = getField("outcomeOutcome")
        assertNotNull(incomeBox)
        assertTrue(incomeBox.size == 1)
        assertEquals(certificateR, incomeBox[0])

        HrDepartment.processNextRequest(hrEmployeeNumber)
        verify { certificateR.process(hrEmployeeNumber) }
        assertNotNull(outcomeOutcome)
        assertTrue(outcomeOutcome.size == 1)
        assertTrue(incomeBox.size == 0)
    }
    @Suppress("UNCHECKED_CAST")
    fun <T> getField(name: String): T = HrDepartment::class.java
        .getDeclaredField(name)
        .apply { isAccessible = true }
        .get(this) as T
}

