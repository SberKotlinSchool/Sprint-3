package ru.sber.qa

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

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
    fun `certificateRequest(MON NDFL) should be added to incomingBox`()
    {
        every { certificateRequest.certificateType } returns CertificateType.NDFL;

        HrDepartment.clock = getClockFromString("2022-11-07T05:00:00.00Z");
        HrDepartment.receiveRequest(certificateRequest);
    }

    @Test
    fun `receiveRequest should throw WeekendDayException`()
    {
        assertThrows<WeekendDayException>
        {
            HrDepartment.clock = getClockFromString("2022-11-05T05:00:00.00Z")
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `receiveRequest should throw NotAllowReceiveRequestException`()
    {
        assertThrows<NotAllowReceiveRequestException>
        {
            every { certificateRequest.certificateType } returns CertificateType.LABOUR_BOOK;
            HrDepartment.clock = getClockFromString("2022-11-07T05:00:00.00Z");
            HrDepartment.receiveRequest(certificateRequest)
        }
    }

    @Test
    fun `processNextRequest should success poll data`()
    {
        every { certificateRequest.certificateType } returns CertificateType.NDFL;
        every { certificateRequest.employeeNumber } returns hrEmployeeNumber;
        every { certificateRequest.process(hrEmployeeNumber) } returns certificate

        HrDepartment.clock = getClockFromString("2022-11-07T05:00:00.00Z");
        HrDepartment.receiveRequest(certificateRequest);

        HrDepartment.processNextRequest(hrEmployeeNumber)
    }
}

fun getClockFromString(str: String): Clock
{
    return Clock.fixed(Instant.parse(str), ZoneId.of("UTC"))
}