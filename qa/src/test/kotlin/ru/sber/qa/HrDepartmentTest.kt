package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

class HrDepartmentTest {
    private lateinit var certRequest : CertificateRequest

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }

    @Test
    fun testHolidays() {
        certRequest = CertificateRequest(1L, CertificateType.NDFL)
        mockDayOfWeek(DayOfWeek.SATURDAY)
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certRequest) }
        mockDayOfWeek(DayOfWeek.SUNDAY)
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certRequest) }
    }

    @Test
    fun testFailInNotWorkDays() {
        testFailInNotWorkDay(listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), CertificateType.NDFL)
        testFailInNotWorkDay(listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY), CertificateType.LABOUR_BOOK)
    }

    @Test
    fun testReceiveRequestSuccess() {
        certRequest = CertificateRequest(1L, CertificateType.NDFL)
        process(certRequest)
    }

    @Test
    fun testProcessNextRequest() {
        val empNo = 1L
        certRequest = mockk()
        every { certRequest.certificateType }.returns(CertificateType.NDFL)
        every { certRequest.process(empNo) }.returns(mockk())
        process(certRequest)
        HrDepartment.processNextRequest(empNo)
        verify { certRequest.process(empNo) }
    }

    private fun process(request: CertificateRequest) {
        mockDayOfWeek(DayOfWeek.MONDAY)
        assertDoesNotThrow { HrDepartment.receiveRequest(request) }
    }

    private fun testFailInNotWorkDay(notWorkDays: List<DayOfWeek>, type: CertificateType) {
        certRequest = CertificateRequest(1L, type)
        for (day in notWorkDays) {
            mockDayOfWeek(day)
            assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certRequest) }
        }
    }

    private fun mockDayOfWeek(day : DayOfWeek) {
        every { LocalDateTime.now(Clock.systemUTC()).dayOfWeek } returns day
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(LocalDateTime::class)
    }



}