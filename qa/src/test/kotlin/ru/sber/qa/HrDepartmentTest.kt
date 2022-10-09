package ru.sber.qa

import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    private val employeeId = abs(Random.nextLong())
    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun init() {
        certificateRequest = spyk(CertificateRequest(employeeId, CertificateType.NDFL))
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestTest() {

    }

    @Test
    fun processNextRequestTest() {

        val hrDepartment = HrDepartment
        val data = Random.nextBytes(100)
        val certificate = Certificate(certificateRequest, employeeId, data)

        hrDepartment.clock = Clock.fixed(Instant.parse("2022-10-10T00:00:00Z"), ZoneOffset.UTC)

        assertFailsWith<NullPointerException> { hrDepartment.processNextRequest(employeeId) }

        every { certificateRequest.process(employeeId) } returns certificate

        hrDepartment.receiveRequest(certificateRequest)
        hrDepartment.processNextRequest(employeeId)

        val incomeBox = getHiddenList<CertificateRequest>(hrDepartment, "incomeBox")
        val outComeBox = getHiddenList<Certificate>(hrDepartment, "outcomeOutcome")
        val processedCertificate = outComeBox[0]

        assert(incomeBox.isEmpty())
        assert(outComeBox.size == 1)
        assertEquals(certificate, processedCertificate)

    }

    private fun <T> getHiddenList(source: HrDepartment, fieldName: String): List<T> {
        val field: Field = HrDepartment.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(source) as List<T>
    }
}