package ru.sber.qa

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList
import kotlin.random.Random

class HrDepartmentTest {

    private val HR_EMPLOYEE_NUMBER = Random.nextLong()
    private val EMPLOYEE_NUMBER = Random.nextLong()
    var certificateRequest: CertificateRequest = mockk()
    var certificate: Certificate = mockk()
    var certificate1: Certificate = mockk()
    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
    }
    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
    @Test
    fun receiveRequestDayCertType_0_0() {
        checkDayCertificateType(0, 0)
    }
    @Test
    fun receiveRequestDayCertType_1_0() {
        checkDayCertificateType(1, 0)
    }
    @Test
    fun receiveRequestDayCertType_2_0() {
        checkDayCertificateType(2, 0)
    }
    @Test
    fun receiveRequestDayCertType_3_0() {
        checkDayCertificateType(3, 0)
    }
    @Test
    fun receiveRequestDayCertType_4_0() {
        checkDayCertificateType(4, 0)
    }
    @Test
    fun receiveRequestDayCertType_5_0() {
        checkDayCertificateType(5, 0)
    }
    @Test
    fun receiveRequestDayCertType_6_0() {
        checkDayCertificateType(6, 0)
    }
    @Test
    fun receiveRequestDayCertType_0_1() {
        checkDayCertificateType(0, 1)
    }
    @Test
    fun receiveRequestDayCertType_1_1() {
        checkDayCertificateType(1, 1)
    }
    @Test
    fun receiveRequestDayCertType_2_1() {
        checkDayCertificateType(2, 1)
    }
    @Test
    fun receiveRequestDayCertType_3_1() {
        checkDayCertificateType(3, 1)
    }
    @Test
    fun receiveRequestDayCertType_4_1() {
        checkDayCertificateType(4,1 )    }
    @Test
    fun receiveRequestDayCertType_5_1() {
        checkDayCertificateType(5, 1)
    }
    @Test
    fun receiveRequestDayCertType_6_1() {
        checkDayCertificateType(6, 1)
    }
    @Test
    fun testOutcomeBox(){
        var field = HrDepartment::class.java.getDeclaredField("incomeBox")
        field.isAccessible = true
        val incomeBox = field.get(HrDepartment) as LinkedList<CertificateRequest>
        every{ certificateRequest.process(HR_EMPLOYEE_NUMBER) } returns certificate
        incomeBox.push(certificateRequest)
        HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER)
        field = HrDepartment::class.java.getDeclaredField("outcomeOutcome")
        field.isAccessible = true
        var outcomeOutcome = field.get(HrDepartment) as LinkedList<Certificate>
        assertEquals(outcomeOutcome[0], certificate)
        assertEquals(outcomeOutcome.size, 1)
        outcomeOutcome.clear()
    }
    fun checkDayCertificateType( day: Int, certificateType: Int ){
        val testDay = DayOfWeek.values()[day]
        val testCertType = CertificateType.values()[certificateType]
        every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns testDay
        every{ certificateRequest.certificateType } returns testCertType
        every{ certificateRequest.employeeNumber } returns EMPLOYEE_NUMBER
        when(testDay){
            in listOf( DayOfWeek.SUNDAY, DayOfWeek.SATURDAY ) -> {
                assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
            }
            in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) ->
                when ( testCertType ){
                    CertificateType.LABOUR_BOOK ->
                        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
                    CertificateType.NDFL -> checkIncomeBox()
                }
            in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY) ->
                when ( testCertType ){
                    CertificateType.LABOUR_BOOK -> checkIncomeBox()
                    CertificateType.NDFL ->
                        assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
                }
        }
    }
    fun checkIncomeBox(){
        HrDepartment.receiveRequest(certificateRequest)
        var field = HrDepartment::class.java.getDeclaredField("incomeBox")
        field.isAccessible = true
        val incomeBox = field.get(HrDepartment) as LinkedList<*>
        assertEquals(incomeBox[0], certificateRequest)
        assertEquals(incomeBox.size, 1)
        incomeBox.clear()
    }
}