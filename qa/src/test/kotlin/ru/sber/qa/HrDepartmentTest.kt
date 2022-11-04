package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals


internal class HrDepartmentTest {


    private val inbox: MutableList<CertificateRequest> = getPrivateFiled(HrDepartment, "incomeBox")
    private val outbox: MutableList<CertificateRequest> = getPrivateFiled(HrDepartment, "outcomeBox")

    @Test
    fun `receiveRequest() should throw weekendException` () {
        // given
        HrDepartment.clock = getWeekendClock()

        // then
        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(mockk())}
    }

    @Test
    fun `receiveRequest() with CertificateType == NDFL should throw NotAllowReceiveRequestException` () {
        // given
        HrDepartment.clock = getTuesdayClock()
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)

        // then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest)}
    }

    @Test
    fun `receiveRequest() with CertificateType == LABOUR_BOOK should throw NotAllowReceiveRequestException` () {
        // given
        HrDepartment.clock = getWednesdayClock()
        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)

        // then
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest)}
    }

    @Test
    fun `receiveRequest() with CertificateType == NDFL should proceed correctly` () {
        // given
        HrDepartment.clock = getWednesdayClock()
        val certificateRequest = CertificateRequest(1L, CertificateType.NDFL)

        // when
        HrDepartment.receiveRequest(certificateRequest)

        // then
        assertEquals(1, inbox.size)
    }

    @Test
    fun `receiveRequest() with CertificateType == LABOUR_BOOK should proceed correctly` () {
        // given
        HrDepartment.clock = getTuesdayClock()
        val certificateRequest = CertificateRequest(1L, CertificateType.LABOUR_BOOK)

        // when
        HrDepartment.receiveRequest(certificateRequest)

        // then
        assertEquals(1, inbox.size)
    }


    @Test
    fun `processNextRequest() should proceed correctly` () {
        // given
        val rq = mockk<CertificateRequest>()
        every { rq.process(any()) } returns mockk()
        inbox.add(rq)

        // when
        HrDepartment.processNextRequest(1L)

        // then
        assertEquals(0, inbox.size)
        assertEquals(1, outbox.size)
    }

    @AfterEach
    fun `clear inbox and outbox lists` () {
        inbox.clear()
        outbox.clear()
    }




}