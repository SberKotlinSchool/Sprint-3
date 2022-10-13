package ru.sber.qa

import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.lang.reflect.Field
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    private val employeeId = abs(Random.nextLong(1L, 100L))
    private lateinit var certificateRequestNDFL: CertificateRequest
    private lateinit var certificateRequestLABOUR: CertificateRequest
    private lateinit var data: ByteArray
    private lateinit var certificate: Certificate
    private lateinit var hrDepartment: HrDepartment

    private val SUNDAY: Clock = Clock.fixed(Instant.parse("2022-10-16T00:00:00Z"), ZoneOffset.UTC)
    private val MONDAY: Clock = Clock.fixed(Instant.parse("2022-10-10T00:00:00Z"), ZoneOffset.UTC)
    private val TUESDAY: Clock = Clock.fixed(Instant.parse("2022-10-11T00:00:00Z"), ZoneOffset.UTC)

    @BeforeEach
    fun init() {
        certificateRequestNDFL = spyk(CertificateRequest(employeeId, CertificateType.NDFL))
        certificateRequestLABOUR = spyk(CertificateRequest(employeeId, CertificateType.LABOUR_BOOK))
        data = Random.nextBytes(100)
        certificate = Certificate(certificateRequestNDFL, employeeId, data)
        hrDepartment = HrDepartment
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestSuccessTest() {

        val incomeBox = getHiddenList<CertificateRequest>(hrDepartment, "incomeBox")

        hrDepartment.clock = MONDAY

        assertDoesNotThrow { hrDepartment.receiveRequest(certificateRequestNDFL) }
        assertEquals(1, incomeBox.size)

        hrDepartment.clock = TUESDAY

        assertDoesNotThrow { hrDepartment.receiveRequest(certificateRequestLABOUR) }
        assertEquals(2, incomeBox.size)
    }

    @Test
    fun receiveRequestWeekendExceptionTest() {

        hrDepartment.clock = SUNDAY

        assertFailsWith<WeekendDayException> { hrDepartment.receiveRequest(certificateRequestNDFL) }
    }

    @Test
    fun receiveRequestNotAllowReceiveRequestExceptionTest() {

        hrDepartment.clock = TUESDAY

        assertFailsWith<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestNDFL) }

        hrDepartment.clock = MONDAY

        assertFailsWith<NotAllowReceiveRequestException> { hrDepartment.receiveRequest(certificateRequestLABOUR) }

    }

    @Test
    fun processNextRequestTest() {

        val incomeBox = getHiddenList<CertificateRequest>(hrDepartment, "incomeBox")
        val outComeBox = getHiddenList<Certificate>(hrDepartment, "outcomeOutcome")

        hrDepartment.clock = MONDAY

        every { certificateRequestNDFL.process(employeeId) } returns certificate

        // NPE when incomeBox is empty
        assertFailsWith<NullPointerException> { hrDepartment.processNextRequest(employeeId) }

        hrDepartment.receiveRequest(certificateRequestNDFL)
        hrDepartment.processNextRequest(employeeId)

        val processedCertificate = outComeBox[0]

        assert(incomeBox.isEmpty())
        assert(outComeBox.size == 1)
        assertEquals(certificate, processedCertificate)

        verify {
            certificateRequestNDFL.certificateType
        }

    }

    private fun <T> getHiddenList(source: HrDepartment, fieldName: String): List<T> {

        val field: Field = HrDepartment.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(source) as List<T>

    }
}