package ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import ru.sber.qa.CertificateType.LABOUR_BOOK
import ru.sber.qa.CertificateType.NDFL
import java.time.*
import java.time.DayOfWeek.*
import java.util.*
import java.util.stream.Stream
import kotlin.random.Random

@Suppress("UNCHECKED_CAST")
internal class HrDepartmentTest {

    companion object {

        @JvmStatic
        fun receiveRequestWeekendTestArguments(): Stream<Arguments> = setOf(SUNDAY, SATURDAY)
            .flatMap { daw ->
                CertificateType.values().map { ct ->
                    Arguments.of(
                        "Given${daw}DayOfWeek${ct}CertificateType_WhenReceiveRequest_thenWeekendDayException",
                        daw,
                        ct
                    )
                }
            }.stream()

        @JvmStatic
        fun receiveRequestTest() = mutableListOf<Arguments>()
            .apply {
                listOf(MONDAY, WEDNESDAY, FRIDAY).forEach { daw1 ->
                    val ct1 = NDFL
                    listOf(TUESDAY, THURSDAY).forEach { daw2 ->
                        val ct2 = LABOUR_BOOK
                        add(Arguments.of(daw1, ct1, daw2, ct2))
                    }
                }
            }.stream()

        @AfterAll
        @JvmStatic
        fun afterAll() {
            unmockkAll()
        }


    }


    private fun getDayOfWeekClock(dayOfWeek: DayOfWeek): Clock {
        val t = LocalDate.of(2022, 11, 6 + dayOfWeek.value)
        return Clock.fixed(
            t.atStartOfDay(ZoneId.of("UTC")).toInstant(),
            ZoneOffset.UTC
        )
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("receiveRequestWeekendTestArguments")
    fun receiveRequestWeekendTest(name: String, dayOfWeek: DayOfWeek, certificateType: CertificateType) {
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek)
        Assertions.assertThrows(WeekendDayException::class.java) {
            HrDepartment.receiveRequest(CertificateRequest(Random.nextLong(), certificateType))
        }
    }

    @ParameterizedTest(name = "givenNDFLCertificateTypeIn{0}Day_whenReceiveRequest_thenCatchNotAllowReceiveRequestException")
    @EnumSource(names = ["TUESDAY", "THURSDAY"])
    fun receiveRequestNDFLCertificateTypeInLABOUR_BOOKDay(dayOfWeek: DayOfWeek) {
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek)
        Assertions.assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(CertificateRequest(Random.nextLong(), NDFL))
        }
    }

    @ParameterizedTest(name = "givenNDFLCertificateTypeIn{0}Day_whenReceiveRequest_thenCatchNotAllowReceiveRequestException")
    @EnumSource(names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
    fun receiveRequestLABOUR_BOOKCertificateTypeInNDFLDay(dayOfWeek: DayOfWeek) {
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek)
        Assertions.assertThrows(NotAllowReceiveRequestException::class.java) {
            HrDepartment.receiveRequest(CertificateRequest(Random.nextLong(), LABOUR_BOOK))
        }
    }

    fun mockCertificateRequest(certificateType: CertificateType) =
        mockkClass(CertificateRequest::class).also {
            every { it.certificateType } returns certificateType
            every { it.employeeNumber } returns Random.nextLong()
            every { it.process(any()) } answers {
                mockkClass(Certificate::class)
            }
        }

    @ParameterizedTest(name = "given{0}DayOfWeekAnd{1}CertificateType_whenReceiveRequest_thenCheckIncomeBox")
    @MethodSource("receiveRequestTest")
    fun receiveRequestTest(
        dayOfWeek1: DayOfWeek, certificateType1: CertificateType,
        dayOfWeek2: DayOfWeek, certificateType2: CertificateType
    ) {
        val incomeBox: LinkedList<CertificateRequest> = getPrivateFiled(HrDepartment, "incomeBox")
        incomeBox.clear()
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek1)
        val cr1 = mockCertificateRequest(certificateType1)
        HrDepartment.receiveRequest(cr1)
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek2)
        val cr2 = mockCertificateRequest(certificateType2)
        HrDepartment.receiveRequest(cr2)

        Assertions.assertEquals(2, incomeBox.size)
        Assertions.assertNotNull(incomeBox.firstOrNull() {
            cr1.certificateType == it.certificateType &&
                    cr1.employeeNumber == it.employeeNumber
        })
        Assertions.assertNotNull(incomeBox.firstOrNull() {
            cr2.certificateType == it.certificateType &&
                    cr2.employeeNumber == it.employeeNumber
        })

    }

    @ParameterizedTest(name = "given{0}DayOfWeekAnd{1}CertificateType_whenReceiveRequest_thenCheckIncomeBox")
    @MethodSource("receiveRequestTest")
    fun processNextRequestTest(
        dayOfWeek1: DayOfWeek, certificateType1: CertificateType,
        dayOfWeek2: DayOfWeek, certificateType2: CertificateType
    ) {
        val incomeBox: LinkedList<CertificateRequest> = getPrivateFiled(HrDepartment, "incomeBox")
        val outcomeOutcome: LinkedList<Certificate> = getPrivateFiled(HrDepartment, "outcomeOutcome")
        incomeBox.clear()
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek1)
        val cr1 = CertificateRequest(100, certificateType1)
        incomeBox.add(cr1)
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek2)
        val cr2 = CertificateRequest(101, certificateType2)
        incomeBox.add(cr2)

        val scanData = Random.nextBytes(150)
        mockkObject(Scanner)
        every { Scanner.getScanData() } returns scanData
        outcomeOutcome.clear()
        val hrEmployeeNumber1 = Random.nextLong()
        HrDepartment.processNextRequest(hrEmployeeNumber1)
        val hrEmployeeNumber2 = Random.nextLong()
        HrDepartment.processNextRequest(hrEmployeeNumber2)
        Assertions.assertTrue(incomeBox.isEmpty())
        Assertions.assertTrue(outcomeOutcome.filterNot {
            it.certificateRequest == cr1 && it.data.contentEquals(scanData) && it.processedBy == hrEmployeeNumber1 ||
                    it.certificateRequest == cr2 && it.data.contentEquals(scanData) && it.processedBy ==
                    hrEmployeeNumber2
        }.isEmpty())

    }

    private fun <T> getPrivateFiled(any: Any, fieldName: String): T {
        return any.javaClass.getDeclaredField(fieldName).let {
            it.isAccessible = true
            return@let it.get(any)
        } as T
    }

}