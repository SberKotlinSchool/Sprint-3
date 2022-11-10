package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.reflect.Field
import java.time.*
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HrDepartmentTest {

    private val employeeNumber: Long = 10
    private val certRequestMock = mockk<CertificateRequest>()
    private val certMock = mockk<Certificate>()
    private val hrDep = spyk(HrDepartment, recordPrivateCalls = true)

    @BeforeEach
    fun setUp() {
        every { certRequestMock.process(employeeNumber) } returns certMock
     }

    @Test
    fun testReceiveRequesSunday() {
        val instant = LocalDateTime.of(2022, 10, 30, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        assertFailsWith(
            WeekendDayException::class
        ) { HrDepartment.receiveRequest(certRequestMock) }
    }

    @Test
    fun testReceiveRequestException() {
        val instant = LocalDateTime.of(2022, 10, 27, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        every { certRequestMock.certificateType } returns CertificateType.NDFL
        assertFailsWith(
            NotAllowReceiveRequestException::class
        ) { HrDepartment.receiveRequest(certRequestMock) }
    }

    @ParameterizedTest(name = "{index} => type={0}, instant={1}")
    @MethodSource("paramProvider")
    fun testReceiveRequest(type: CertificateType, instant: Instant) {
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        every { certRequestMock.certificateType } returns type
        val incomeBox = getIncomeBoxProperty().get(HrDepartment) as LinkedList<CertificateRequest>
        val incomeBoxCountIn = incomeBox.count()
        HrDepartment.receiveRequest(certRequestMock)
        val incomeBoxCountOut = incomeBox.count()
        assertEquals(incomeBoxCountIn + 1, incomeBoxCountOut)
    }

    private fun paramProvider(): Stream<Arguments?> =  Stream.of(
        Arguments.of(CertificateType.NDFL, LocalDateTime.of(2022, 10, 31, 15, 8, 0).toInstant(ZoneOffset.UTC)),
        Arguments.of(CertificateType.LABOUR_BOOK, LocalDateTime.of(2022, 11, 10, 15, 8, 0).toInstant(ZoneOffset.UTC))
    )

    @Test
    fun testProcessNextRequest() {
        val instant = LocalDateTime.of(2022, 10, 31, 15, 8, 0).toInstant(ZoneOffset.UTC)
        HrDepartment.clock = Clock.fixed(instant, ZoneId.of("UTC"))
        every { certRequestMock.certificateType } returns CertificateType.NDFL
        val incomeBox = getIncomeBoxProperty().get(HrDepartment) as LinkedList<CertificateRequest>
        incomeBox.push(certRequestMock)
        HrDepartment.processNextRequest(employeeNumber)
        verify(exactly = 1) { certRequestMock.process(employeeNumber) }
    }

    private fun getIncomeBoxProperty(): Field {
        val prop = HrDepartment.javaClass.getDeclaredField("incomeBox")
        prop.isAccessible = true
        return prop
    }

}