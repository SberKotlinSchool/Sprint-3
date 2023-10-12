package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

class HrDepartmentTest {

    private val HR_EMPLOYEE_NUMBER = Random.nextLong()
    private val EMPLOYEE_NUMBER = Random.nextLong()

    private val certificateRequest: CertificateRequest = mockk()
    private val certificate: Certificate = mockk()

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test() {
        for (dayOfWeek in DayOfWeek.values()) {
            println(dayOfWeek)
            every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns dayOfWeek
            for (certificateType in CertificateType.values()) {
                println(certificateType)
                every { certificateRequest.certificateType } returns certificateType
                every { certificateRequest.employeeNumber } returns EMPLOYEE_NUMBER
                every { certificateRequest.process(HR_EMPLOYEE_NUMBER) } returns certificate

                when (dayOfWeek) {
                    in listOf(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY) -> {
                        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
                    }
                    in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) -> {
                        when (certificateType) {
                            CertificateType.LABOUR_BOOK ->
                                assertThrows<NotAllowReceiveRequestException> {
                                    HrDepartment.receiveRequest(
                                        certificateRequest
                                    )
                                }
                            CertificateType.NDFL -> checkPrivateFields()
                        }
                    }
                    in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY) -> {
                        when (certificateType) {
                            CertificateType.LABOUR_BOOK -> checkPrivateFields()
                            CertificateType.NDFL ->
                                assertThrows<NotAllowReceiveRequestException> {
                                    HrDepartment.receiveRequest(
                                        certificateRequest
                                    )
                                }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun checkPrivateFields() {
        HrDepartment.receiveRequest(certificateRequest)
        var field = HrDepartment::class.java.getDeclaredField("incomeBox")
        field.isAccessible = true
        val incomeBox = field.get(HrDepartment) as LinkedList<*>
        assertEquals(incomeBox[0], certificateRequest)
        assertEquals(incomeBox.size, 1)
        HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER)
        field = HrDepartment::class.java.getDeclaredField("outcomeOutcome")
        field.isAccessible = true
        val outcomeOutcome = field.get(HrDepartment) as LinkedList<*>
        assertEquals(outcomeOutcome[0], certificate)
        assertEquals(outcomeOutcome.size, 1)
        outcomeOutcome.clear()
    }
}
