package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.time.Clock
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.util.stream.Stream
import kotlin.jvm.Throws
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class HrDepartmentTest {

    val MONDAY: String = "2022-10-31T00:15:30.00Z"
    val TUESDAY: String = "2022-11-01T00:15:30.00Z"
    val WEDNESDAY: String = "2022-11-02T00:15:30.00Z"
    val THURSDAY: String = "2022-11-03T00:15:30.00Z"
    val FRIDAY: String = "2022-11-04T00:15:30.00Z"

    val certRequestNdfl = mockk<CertificateRequest>()
    val certRequestLabourBook = mockk<CertificateRequest>()

    @Before
    fun setUp() {
        mockkObject(HrDepartment)

        every { certRequestNdfl.employeeNumber } returns 1
        every { certRequestNdfl.certificateType } returns CertificateType.NDFL

        every { certRequestLabourBook.employeeNumber } returns 2
        every { certRequestLabourBook.certificateType } returns CertificateType.LABOUR_BOOK
    }

    @Test
    fun ndflRequestShouldGetWeekendDayException() {
        assertFailsWith<WeekendDayException>("Не получено корректное исключение WeekendDayException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse("2022-10-30T10:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)
        }
    }

    @Test
    fun labourBookRequestShouldGetWeekendDayException() {
        assertFailsWith<WeekendDayException>("Не получено корректное исключение WeekendDayException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse("2022-10-30T10:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        }
    }

    @Test
    //переделать  на параметризированный тест  с другими днями  недели  по которым не выдают labour_book
    fun labourBookRequestShouldGetNotAllowedException() {
        assertFailsWith<NotAllowReceiveRequestException>("Не получено корректное исключение NotAllowReceiveRequestException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse("2022-10-31T00:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        }
    }

    @Test
    //переделать  на параметризированный тест  с другими днями  недели  по которым не выдают ndfl
    fun ndflRequestShouldGetNotAllowedException() {
        assertFailsWith<NotAllowReceiveRequestException>("Не получено корректное исключение NotAllowReceiveRequestException ") {
            HrDepartment.clock = Clock.fixed(Instant.parse("2022-11-01T00:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)
        }
    }

    @Test
    //переделать  на параметризированный тест  с другими днями  недели  по которым выдают ndfl
    fun ndflRequestShouldNotGetException() {
            HrDepartment.clock = Clock.fixed(Instant.parse("2022-10-31T00:15:30.00Z"), ZoneId.of("Asia/Calcutta"));
            HrDepartment.receiveRequest(certificateRequest = certRequestNdfl)

    }

//    companion object {
//        @JvmStatic
//        fun getValue(): Stream<Arguments> {
//            return Stream.of(
//                Arguments.of("2022-10-31T00:15:30.00Z",
//                Arguments.of("2022-10-31T00:15:30.00Z")
//            ))
//        }
//    }
}