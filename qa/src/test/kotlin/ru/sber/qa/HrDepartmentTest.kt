package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.random.Random

internal class HrDepartmentTest {

    val random = Random(13)
    var hrDepartment = spyk<HrDepartment>()
    val localDateTime = mockk<LocalDateTime>()

    @Test
    fun receiveRequest() {
        every { localDateTime.dayOfWeek } returnsMany listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY)
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(CertificateRequest(random.nextLong(), CertificateType.NDFL)) }
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(CertificateRequest(random.nextLong(), CertificateType.NDFL)) }
        assertThrows<WeekendDayException> { hrDepartment.receiveRequest(CertificateRequest(random.nextLong(), CertificateType.NDFL)) }
    }

    @Test
    fun processNextRequest() {
    }
}