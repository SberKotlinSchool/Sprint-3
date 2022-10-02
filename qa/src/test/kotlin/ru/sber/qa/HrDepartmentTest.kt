package ru.sber.qa

import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.*
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals

internal class HrDepartmentTest {

    @Test
    fun testReceiveRequestWeekDays() {
        for (weekendNum in 0..1) {
            clock(17, weekendNum)
            assertThrows<WeekendDayException> {
                HrDepartment.receiveRequest(CertificateRequest(Random.nextLong(), CertificateType.NDFL))
            }
        }
    }

    private fun clock(baseDayNum: Int, weekendNum: Int) {
        HrDepartment.clock = Clock.fixed(
            Instant.parse("2022-09-${baseDayNum + weekendNum}T00:00:00Z"), ZoneId.of("UTC")
        )
    }

    @Test
    fun testReceiveRequestNDFLWorkDays() {
        test(CertificateType.NDFL, { r -> receiveRequestWithoutException(r) }, { r -> receiveRequestWithException(r) })
    }

    @Test
    fun testReceiveRequestLabourBookWorkDays() {
        test(
            CertificateType.LABOUR_BOOK,
            { r -> receiveRequestWithException(r) },
            { r -> receiveRequestWithoutException(r) }
        )
    }

    @Test
    fun testProcessNextRequest() {
        val linkedList = LinkedList<CertificateRequest>()
        val mockkClass = mockkClass(CertificateRequest::class)
        val expectedCertificate = Certificate(
            CertificateRequest(Random.nextLong(), CertificateType.LABOUR_BOOK), Random.nextLong(), ByteArray(1)
        )
        every { mockkClass.process(any()) } returns expectedCertificate

        linkedList.push(mockkClass)

        revokeFinalModifierFromField(HrDepartment::class.java, "incomeBox").set(HrDepartment, linkedList)

        HrDepartment.processNextRequest(Random.nextLong())

        val result = revokeFinalModifierFromField(HrDepartment::class.java, "outcomeOutcome")
            .get(HrDepartment) as LinkedList<*>
        assertEquals(result[0], expectedCertificate)
    }

    private fun <T> revokeFinalModifierFromField(clazz: Class<T>, fieldName: String): Field {
        val modifiedField = clazz.getDeclaredField(fieldName)
        modifiedField.isAccessible = true
        val modifiers = Field::class.java.getDeclaredField("modifiers")
        modifiers.isAccessible = true
        modifiers.setInt(modifiedField, modifiedField.modifiers and Modifier.FINAL.inv())
        return modifiedField
    }

    private fun test(type: CertificateType, f1: (r: CertificateRequest) -> Unit, f2: (r: CertificateRequest) -> Unit) {
        val request = CertificateRequest(Random.nextLong(), type)

        for (workDayNum in 0..4) {
            clock(19, workDayNum)
            if (workDayNum % 2 == 0) f1.invoke(request) else f2.invoke(request)
        }
    }

    private fun receiveRequestWithoutException(request: CertificateRequest) {
        HrDepartment.receiveRequest(request)
        val result = revokeFinalModifierFromField(HrDepartment::class.java, "incomeBox")
            .get(HrDepartment) as LinkedList<*>
        assertEquals(result[0], request)
    }

    private fun receiveRequestWithException(request: CertificateRequest) {
        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(request) }
    }

}