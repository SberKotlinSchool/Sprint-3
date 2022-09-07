package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.random.Random

internal class HrDepartmentTest {

    @Test
    fun `test`() {
        mockkObject(Random.Default)
        mockkStatic(LocalDateTime::class)

        every { LocalDateTime.now().dayOfWeek } returns DayOfWeek.SATURDAY

        assertEquals(DayOfWeek.SATURDAY, LocalDateTime.now().dayOfWeek)

        //HrDepartment.receiveRequest(CertificateRequest(1L, CertificateType.NDFL))
        //HrDepartment.processNextRequest(1L)
    }
}