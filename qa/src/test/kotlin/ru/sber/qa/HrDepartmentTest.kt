package ru.sber.qa

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkClass
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
internal class HrDepartmentTest {
    private val employeeId = 1L
    private val data = byteArrayOf(0, 1, 2)
    private val certificateRequest = mockkClass(CertificateRequest::class)
    private val incomeBoxField = HrDepartment.javaClass.getDeclaredField("incomeBox")
    private val outcomeBoxField = HrDepartment.javaClass.getDeclaredField("outcomeOutcome")

    @BeforeEach
    fun setUp() {
        // Будний день у HR департамента по умолчанию
        HrDepartment.clock = Clock.fixed(
            LocalDate.of(2021, 9, 3).atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )

        every { certificateRequest.certificateType } returns CertificateType.NDFL
        every { certificateRequest.employeeNumber } returns employeeId
    }

    @Test
    fun receiveRequest() {
        HrDepartment.receiveRequest(certificateRequest)

        incomeBoxField.trySetAccessible()
        val incomeBox = incomeBoxField.get(HrDepartment) as List<*>
        assertNotNull(incomeBox)
        assertFalse { incomeBox.isEmpty() }
        assertEquals(certificateRequest, incomeBox[0] as CertificateRequest)
    }

    @Test
    fun `receiveRequest with WeekendDayException`() {
        HrDepartment.clock = Clock.fixed(
            LocalDate.of(2021, 9, 5).atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )

        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest with NotAllowReceiveRequestException for LABOUR_BOOK`() {
        HrDepartment.clock = Clock.fixed(
            LocalDate.of(2021, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )

        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun `receiveRequest with NotAllowReceiveRequestException for NDFL`() {
        HrDepartment.clock = Clock.fixed(
            LocalDate.of(2021, 9, 2).atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )

        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun processNextRequest() {
        val certificate = mockkClass(Certificate::class)
        every { certificateRequest.process(employeeId) } returns certificate

        HrDepartment.receiveRequest(certificateRequest)
        HrDepartment.processNextRequest(employeeId)

        outcomeBoxField.trySetAccessible()
        val outcomeBox = outcomeBoxField.get(HrDepartment) as List<*>
        assertNotNull(outcomeBox)
        assertFalse { outcomeBox.isEmpty() }
        assertEquals(certificate, outcomeBox[0] as Certificate)
    }

    @AfterEach
    fun tearDown() {
        HrDepartment.clock = Clock.systemUTC()
    }
}