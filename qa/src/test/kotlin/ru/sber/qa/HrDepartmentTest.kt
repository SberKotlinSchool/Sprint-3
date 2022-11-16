package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*
import kotlin.random.Random
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    val MONDAY = LocalDate.of(2022, Month.NOVEMBER, 14)
    val TUESDAY = LocalDate.of(2022, Month.NOVEMBER, 15)
    val WEDNESDAY = LocalDate.of(2022, Month.NOVEMBER, 16)
    val THURSDAY = LocalDate.of(2022, Month.NOVEMBER, 17)
    val FRIDAY = LocalDate.of(2022, Month.NOVEMBER, 18)
    val SATURDAY = LocalDate.of(2022, Month.DECEMBER, 31)
    val SUNDAY = LocalDate.of(2023, Month.JANUARY, 1)

    @BeforeEach
    internal fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `receiveRequest throws WeekendDayException`() {
        assertFailsWith(
            exceptionClass = WeekendDayException::class,
            block = {
                val certificateRequest = CertificateRequest(1L, CertificateType.values().random())
                listOf(SATURDAY, SUNDAY).forEach {
                    HrDepartment.clock = getClock(it)
                    HrDepartment.receiveRequest(certificateRequest)
                }
            }
        )
    }

    @Test
    fun `receiveRequest throws NotAllowReceiveRequestException`() {
        assertFailsWith(
            exceptionClass = NotAllowReceiveRequestException::class,
            block = {
                var certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
                listOf(TUESDAY, THURSDAY).forEach {
                    HrDepartment.clock = getClock(it)
                    HrDepartment.receiveRequest(certificateRequest)
                }
                certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
                listOf(MONDAY, WEDNESDAY, FRIDAY).forEach {
                    HrDepartment.clock = getClock(it)
                    HrDepartment.receiveRequest(certificateRequest)
                }
            }
        )
    }


    @Test
    fun `receiveRequest not throws NotAllowReceiveRequestException`() {

        var certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        listOf(TUESDAY, THURSDAY).forEach {
            HrDepartment.clock = getClock(it)
            assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        }
        certificateRequest = CertificateRequest(1L, CertificateType.NDFL)
        listOf(MONDAY, WEDNESDAY, FRIDAY).forEach {
            HrDepartment.clock = getClock(it)
            assertDoesNotThrow { HrDepartment.receiveRequest(certificateRequest) }
        }
    }

    @Test
    fun `receiveRequest outcomeOutcome is not empty`() {

        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)
        HrDepartment.clock = getClock(TUESDAY)
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 5000L
        val byteArray = Random.nextBytes(100)
        every { Random.nextBytes(100) } returns byteArray

        HrDepartment.receiveRequest(certificateRequest)
        HrDepartment.processNextRequest(2L)
        val outcomeBox: LinkedList<*>

        try {
            val field = HrDepartment::class.java.getDeclaredField("outcomeOutcome")
            field.isAccessible = true
            outcomeBox = field.get(HrDepartment) as LinkedList<*>
            //проверка, что не кидает NPE
            assertDoesNotThrow { outcomeBox.poll() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun getClock(date: LocalDate) =
        Clock.fixed(date.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
}