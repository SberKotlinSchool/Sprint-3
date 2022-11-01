package ru.sber.qa


import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    private val hrDepartment = HrDepartment
    @SpyK
    val ct1 : CertificateType = CertificateType.NDFL
    @SpyK
    val ct2 : CertificateType = CertificateType.LABOUR_BOOK
    @MockK
    lateinit var cr1 : CertificateRequest
    @MockK
    lateinit var cr2 : CertificateRequest

    @BeforeEach
    internal fun setUp() {
        cr1 = mockk(relaxed = true)
        cr2 = mockk(relaxed = true)
    }

    @Test
    fun receiveRequest() {
        hrDepartment.clock = Clock.fixed(
            Instant.parse("2022-10-30T10:15:30.00Z"), ZoneId.of("Asia/Yerevan"))

        assertFailsWith(exceptionClass =  WeekendDayException::class,
                        block =  {
                            hrDepartment.receiveRequest(cr1)
                        })
        assertFailsWith(exceptionClass =  NotAllowReceiveRequestException::class,
            block =  {
                throw NotAllowReceiveRequestException()
            })
    }

    @Test
    fun processNextRequest() {

        hrDepartment.clock = Clock.fixed(
            Instant.parse("2022-10-31T10:15:30.00Z"), ZoneId.of("Asia/Yerevan"))
        hrDepartment.receiveRequest(cr1)
        hrDepartment.processNextRequest(1)
    }
}