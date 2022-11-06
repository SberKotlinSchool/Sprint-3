package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.*
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
        fun receiveRequestAndProcessNextRequestTest() = mutableListOf<Arguments>()
            .apply {
                addAll(
                    listOf(MONDAY, WEDNESDAY, FRIDAY).map { daw ->
                        Arguments.of(daw, NDFL)
                    })
                addAll(
                    listOf(TUESDAY, THURSDAY).map { daw ->
                        Arguments.of(daw, LABOUR_BOOK)
                    })
            }.stream()


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

    @ParameterizedTest(name = "given{0}DayOfWeekAnd{1}CertificateType_whenReceiveRequestAndProcessNext_thenCertificateRequestProcess")
    @MethodSource("receiveRequestAndProcessNextRequestTest")
    fun receiveRequestAndProcessNextRequestTest(dayOfWeek: DayOfWeek, certificateType: CertificateType) {
        HrDepartment.clock = getDayOfWeekClock(dayOfWeek)
        val certificateRequest1 = mockCertificateRequest(certificateType)
        val certificateRequest2 = mockCertificateRequest(certificateType)
        HrDepartment.receiveRequest(certificateRequest1)
        HrDepartment.receiveRequest(certificateRequest2)
        val hrEmployeeNumber = Random.nextLong()
        HrDepartment.processNextRequest(hrEmployeeNumber)
        HrDepartment.processNextRequest(hrEmployeeNumber)
        verify(exactly = 1) { certificateRequest1.process(hrEmployeeNumber) }
        verify(exactly = 1) { certificateRequest2.process(hrEmployeeNumber) }
    }

//    @ParameterizedTest(name = "Given{0}DayOfWeek_WhenReceiveRequest_thenIncomeBox")
//    @EnumSource(names = ["MONDAY", "WEDNESDAY", "FRIDAY"])
//    fun receiveRequestNdflCertificateTypeSuccessTest(
//        dayOfWeek: DayOfWeek
//    ) {
//        mockkConstructor(LinkedList::class)
//        mockkObject(HrDepartment)
//        lateinit var certReq: CertificateRequest
//        every {
//            anyConstructed<LinkedList<CertificateRequest>>().push(any())
//        } answers {
//            certReq = it.invocation.args[0] as CertificateRequest
//        }
//        HrDepartment.clock = getDayOfWeekClock(dayOfWeek)
//        val certRequest = CertificateRequest(Random.nextLong(), NDFL)
//        HrDepartment.receiveRequest(certificateRequest = certRequest)
//
//        unmockkObject(HrDepartment)
//        unmockkConstructor(LinkedList::class)
//
//        Assertions.assertEquals(certRequest, certReq)
//
//    }

}