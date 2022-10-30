package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    val certRequestNdfl = mockk<CertificateRequest>()
    val certRequestLabourBook = mockk<CertificateRequest>()

    @BeforeEach
    fun setUp() {
        mockkObject(HrDepartment)

        every { certRequestNdfl.employeeNumber } returns 1
        every { certRequestNdfl.certificateType } returns CertificateType.NDFL

        every { certRequestLabourBook.employeeNumber } returns 2
        every { certRequestLabourBook.certificateType } returns CertificateType.LABOUR_BOOK
    }

    @ParameterizedTest
    @MethodSource("getWeekend")
    fun ndflRequestShouldGetWeekendDayException(dayOfWeek: String) {
        assertFailsWith<WeekendDayException>("Не получено корректное исключение WeekendDayException") {
            HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)
        }
    }

    @ParameterizedTest
    @MethodSource("getWeekend")
    fun labourBookRequestShouldGetWeekendDayException(dayOfWeek: String) {
        assertFailsWith<WeekendDayException>("Не получено корректное исключение WeekendDayException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        }
    }

    @ParameterizedTest
    @MethodSource("getMonWedFri")
    fun labourBookRequestShouldGetNotAllowedException(dayOfWeek: String) {
        assertFailsWith<NotAllowReceiveRequestException>("Не получено корректное исключение NotAllowReceiveRequestException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        }
    }

    @ParameterizedTest
    @MethodSource("getTueThu")
    fun ndflRequestShouldGetNotAllowedException(dayOfWeek: String) {
        assertFailsWith<NotAllowReceiveRequestException>("Не получено корректное исключение NotAllowReceiveRequestException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)
        }
    }

    @ParameterizedTest
    @MethodSource("getMonWedFri")
    fun ndflRequestShouldNotGetException(dayOfWeek: String) {
        HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
        HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)
    }

    @ParameterizedTest
    @MethodSource("getTueThu")
    fun labourBookRequestShouldNotGetException(dayOfWeek: String) {
        HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
        HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        verify (exactly = 1){ HrDepartment.receiveRequest(certRequestLabourBook) }
    }

    @Test
    fun labourBookRequestShouldNotGetException1() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-11-01T00:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
        HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        HrDepartment.processNextRequest(certRequestLabourBook.employeeNumber)
       // verify (exactly = 1){ HrDepartment.processNextRequest(certRequestLabourBook.employeeNumber) }

    }

    companion object {
        @JvmStatic
        fun getMonWedFri() = listOf(
            Arguments.of("2022-10-31T00:15:30.00Z"),
            Arguments.of("2022-11-02T00:15:30.00Z"),
            Arguments.of("2022-11-04T00:15:30.00Z")
        )

        @JvmStatic
        fun getTueThu() = listOf(
            Arguments.of("2022-11-01T00:15:30.00Z"),
            Arguments.of("2022-11-03T00:15:30.00Z")
        )

        @JvmStatic
        fun getWeekend() = listOf(
            Arguments.of("2022-11-05T00:15:30.00Z"),
            Arguments.of("2022-11-06T00:15:30.00Z")
        )
    }
}
