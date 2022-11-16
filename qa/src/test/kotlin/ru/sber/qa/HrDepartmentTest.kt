package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.random.Random
import kotlin.test.*

internal class HrDepartmentTest {
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

    val certRequestNdfl = mockk<CertificateRequest>()
    val certRequestLabourBook = mockk<CertificateRequest>()

    val random = Random.nextBytes(1)

    @BeforeEach
    fun setUp() {
        every { certRequestNdfl.employeeNumber } returns 1
        every { certRequestNdfl.certificateType } returns CertificateType.NDFL

        every { certRequestLabourBook.employeeNumber } returns 2
        every { certRequestLabourBook.certificateType } returns CertificateType.LABOUR_BOOK
        every { certRequestLabourBook.process(1) } returns Certificate(certRequestLabourBook, 1, random)
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
        val incomeBox: LinkedList<String> = getPrivateField("incomeBox")
        assertNotNull(
            incomeBox,
            "Количество элементов в incomeBox не должно быть  null, а по факту равно ${incomeBox.size}"
        )
    }

    @ParameterizedTest
    @MethodSource("getTueThu")
    fun labourBookRequestShouldNotGetException(dayOfWeek: String) {
        HrDepartment.clock = Clock.fixed(Instant.parse(dayOfWeek), ZoneId.of("Asia/Calcutta"));
        HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        val incomeBox: LinkedList<String> = getPrivateField("incomeBox")
        assertNotNull(
            incomeBox,
            "Количество элементов в incomeBox не должно быть  null, а по факту равно ${incomeBox.size}"
        )
    }

    @Test
    fun checkProcessNextRequestWorks() {
        HrDepartment.clock = Clock.fixed(Instant.parse("2022-11-01T00:15:30.00Z"), ZoneId.of("Europe/Moscow"));

        HrDepartment.receiveRequest(certificateRequest = certRequestLabourBook)
        val incomeBox: LinkedList<String> = getPrivateField("incomeBox")
        assertNotNull(
            incomeBox,
            "Количество элементов в incomeBox не должно быть  null, а по факту равно ${incomeBox.size}"
        )

        HrDepartment.processNextRequest(hrEmployeeNumber = 1)
        val outcomeBox = getPrivateField<LinkedList<String>>("outcomeBox")

        assertNotNull(
            outcomeBox,
            "Количество элементов outcomeBox  не должно быть  null, а по факту равно ${outcomeBox.size}"
        )
        assertTrue(outcomeBox.size == 1, "Количество элементов в outcomeBox не соответствует ожидаемому")
        assertTrue(
            incomeBox.size == 0,
            "Количество элементов incomeBox   должно быть  0, а по факту равно ${incomeBox.size}"
        )
    }

    fun <T> getPrivateField(name: String): T =
        HrDepartment::class.java
            .getDeclaredField(name)
            .apply { isAccessible = true }
            .get(this) as T
}
