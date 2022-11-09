package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.assertTrue

internal class HrDepartmentTest {

    private val hrEmployeeNumber = 200L
    private lateinit var certificateRequest: CertificateRequest;
    private lateinit var certificate: Certificate;

    @BeforeEach
    internal fun setUp() {
        mockkObject(HrDepartment)
        certificateRequest = mockk()
        certificate = mockk()
    }

    @AfterEach
    internal fun tearDown() {
        unmockkAll()
    }

    @Test()
    fun `certificateRequest(MON NDFL) should be added to incomingBox`() {
        every { certificateRequest.certificateType } returns CertificateType.NDFL;

        HrDepartment.clock = getClockFromString("2022-11-07T05:00:00.00Z");
        HrDepartment.receiveRequest(certificateRequest);
    }

    @Test
    fun `receiveRequest should throw WeekendDayException`() {
        assertThrows<WeekendDayException>
        {
            HrDepartment.clock = getClockFromString("2022-11-05T05:00:00.00Z")
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException`() {
        assertThrows<NotAllowReceiveRequestException>
        {
            every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK;
            HrDepartment.clock = getClockFromString("2022-11-07T05:00:00.00Z");
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `processNextRequest should success poll data`() {
        val outcomeOutcome: LinkedList<Certificate> = LinkedList()
        val incomeBox: LinkedList<CertificateRequest> = LinkedList()

        incomeBox.add(certificateRequest)

        HrDepartment.mockPrivateFields(outcomeOutcome, "outcomeOutcome")
        HrDepartment.mockPrivateFields(incomeBox, "incomeBox")

        every { certificateRequest.process(any()) } returns certificate

        HrDepartment.processNextRequest(hrEmployeeNumber)

        assertTrue { outcomeOutcome.isNotEmpty() }
        assertTrue { incomeBox.isEmpty() }
    }
}

fun getClockFromString(str: String): Clock {
    return Clock.fixed(Instant.parse(str), ZoneId.of("UTC"))
}

fun Any.mockPrivateFields(mock: Any, fieldName: String): Any {
    javaClass.declaredFields
        .filter { it.modifiers.and(Modifier.PRIVATE) > 0 || it.modifiers.and(Modifier.PROTECTED) > 0 }
        .firstOrNull { it.name == fieldName }
        ?.also { it.isAccessible = true }
        ?.also { it ->
            run {
                val modifiers = Field::class.java.getDeclaredField("modifiers")
                modifiers.isAccessible = true
                modifiers.setInt(it, it.modifiers and Modifier.FINAL.inv())
            }
        }
        ?.set(this, mock)
    return this
}