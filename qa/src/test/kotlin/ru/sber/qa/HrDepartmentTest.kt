package ru.sber.qa

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList
import kotlin.test.assertEquals

internal class HrDepartmentTest {
    @MockK
    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun receiveRequestNegativeTest() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.SATURDAY
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestNegativeTest2() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestNegativeTest3() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestPositiveTest1() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.MONDAY
        every { certificateRequest.certificateType } returns CertificateType.NDFL
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun receiveRequestPositiveTest2() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now(HrDepartment.clock).dayOfWeek } returns DayOfWeek.THURSDAY
        every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK
        assertDoesNotThrow {
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun processNextRequest() {
        val incomeBox:  LinkedList<CertificateRequest> = LinkedList()
        val outcomeOutcome: LinkedList<Certificate> = LinkedList()

        val incomeBoxTest: LinkedList<CertificateRequest>
        HrDepartment::class.java.getDeclaredField("incomeBox").let {
            it.isAccessible = true
            incomeBoxTest = it.get(incomeBox) as LinkedList<CertificateRequest>
        }

        val outcomeOutcomeTest: LinkedList<Certificate>
        HrDepartment::class.java.getDeclaredField("outcomeOutcome").let {
            it.isAccessible = true
            outcomeOutcomeTest = it.get(outcomeOutcome) as LinkedList<Certificate>
        }

        incomeBoxTest.push(certificateRequest)

        val certificate = Certificate(certificateRequest, 123L, ByteArray(1))
        every { certificateRequest.process(any()) } returns certificate

        HrDepartment.processNextRequest(123L)

        verify {
            certificateRequest.process(123L)
        }
        assertEquals(outcomeOutcomeTest.first, certificate)
    }
}