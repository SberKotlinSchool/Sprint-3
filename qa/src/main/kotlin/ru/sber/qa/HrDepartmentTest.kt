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
    fun test() {
        for ( i in DayOfWeek.values()){
            println(i)
            every { LocalDateTime.now(any<Clock>()).dayOfWeek } returns i
            for ( type in CertificateType.values()){
                println(type)
                every{ certificateRequest.certificateType } returns type
                every{ certificateRequest.employeeNumber } returns EMPLOYEE_NUMBER
                every{ certificateRequest.process(HR_EMPLOYEE_NUMBER) } returns certificate
                when(i){
                    in listOf( DayOfWeek.SUNDAY, DayOfWeek.SATURDAY ) -> {
                        assertThrows<WeekendDayException> { HrDepartment.receiveRequest(certificateRequest) }
                    }
                    in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY) ->
                        when ( type ){
                            CertificateType.LABOUR_BOOK ->
                                assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
                            CertificateType.NDFL -> checkPrivateFields()
                        }
                    in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY) ->
                        when ( type ){
                            CertificateType.LABOUR_BOOK -> checkPrivateFields()
                            CertificateType.NDFL ->
                                assertThrows<NotAllowReceiveRequestException> { HrDepartment.receiveRequest(certificateRequest) }
                        }
                }
            }
        }
    }

    fun checkPrivateFields(){
        HrDepartment.receiveRequest(certificateRequest)
        var field = HrDepartment::class.java.getDeclaredField("incomeBox")
        field.isAccessible = true
        val incomeBox = field.get(HrDepartment) as LinkedList<*>
        assertEquals(incomeBox[0], certificateRequest)
        assertEquals(incomeBox.size, 1)
        HrDepartment.processNextRequest(HR_EMPLOYEE_NUMBER)
        field = HrDepartment::class.java.getDeclaredField("outcomeOutcome")
        field.isAccessible = true
        var outcomeOutcome = field.get(HrDepartment) as LinkedList<Certificate>
        assertEquals(outcomeOutcome[0], certificate)
        assertEquals(outcomeOutcome.size, 1)
        outcomeOutcome.clear()
    }

}