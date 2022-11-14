package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.sber.qa.CertificateType.LABOUR_BOOK
import ru.sber.qa.CertificateType.NDFL
import java.time.*
import java.util.*
import kotlin.random.Random

internal class HrDepartmentTest {

    val TEST_HR_EMPLOYEE_NUMBER = 2193L
    val TEST_EMPLOYEE_NUMBER = 2253L

    @Test
    fun receiveRequestTest_MondayNDFL_received() {
        //setup
        val hrDepartment = HrDepartment
        val mondayClock = Clock.fixed(Instant.parse("2022-11-07T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = mondayClock
        hrDepartment.receiveRequest(certificateRequest = certificateRequest)
        val incomeBox: LinkedList<*>
        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("incomeBox")
            field.isAccessible = true
            incomeBox = field.get(hrDepartment) as LinkedList<*>
            assertEquals(incomeBox[0], certificateRequest)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun receiveRequestTest_MondayLaborBook_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val mondayClock = Clock.fixed(Instant.parse("2022-11-07T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = mondayClock
        assertThrows<NotAllowReceiveRequestException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_TuesdayLabourBook_received() {
        //setup
        val hrDepartment = HrDepartment
        val tuesdayClock = Clock.fixed(Instant.parse("2022-11-08T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = tuesdayClock
        hrDepartment.receiveRequest(certificateRequest = certificateRequest)
        val incomeBox: LinkedList<*>
        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("incomeBox")
            field.isAccessible = true
            incomeBox = field.get(hrDepartment) as LinkedList<*>
            assertEquals(incomeBox[0], certificateRequest)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun receiveRequestTest_TuesdayNDFL_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val tuesdayClock = Clock.fixed(Instant.parse("2022-11-08T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = tuesdayClock
        assertThrows<NotAllowReceiveRequestException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_WednesdayNDFL_received() {
        //setup
        val hrDepartment = HrDepartment
        val wednesdayClock = Clock.fixed(Instant.parse("2022-11-09T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = wednesdayClock
        hrDepartment.receiveRequest(certificateRequest = certificateRequest)
        val incomeBox: LinkedList<*>
        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("incomeBox")
            field.isAccessible = true
            incomeBox = field.get(hrDepartment) as LinkedList<*>
            assertEquals(incomeBox[0], certificateRequest)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun receiveRequestTest_WednesdayLaborBook_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val wednesdayClock = Clock.fixed(Instant.parse("2022-11-09T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = wednesdayClock
        assertThrows<NotAllowReceiveRequestException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_ThursdayLabourBook_received() {
        //setup
        val hrDepartment = HrDepartment
        val thursdayClock = Clock.fixed(Instant.parse("2022-11-10T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = thursdayClock
        hrDepartment.receiveRequest(certificateRequest = certificateRequest)
        val incomeBox: LinkedList<*>
        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("incomeBox")
            field.isAccessible = true
            incomeBox = field.get(hrDepartment) as LinkedList<*>
            assertEquals(incomeBox[0], certificateRequest)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun receiveRequestTest_ThursdayNDFL_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val thursdayClock = Clock.fixed(Instant.parse("2022-11-10T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = thursdayClock
        assertThrows<NotAllowReceiveRequestException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_FridayNDFL_received() {
        //setup
        val hrDepartment = HrDepartment
        val fridayClock = Clock.fixed(Instant.parse("2022-11-11T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = fridayClock
        hrDepartment.receiveRequest(certificateRequest = certificateRequest)
        val incomeBox: LinkedList<*>
        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("incomeBox")
            field.isAccessible = true
            incomeBox = field.get(hrDepartment) as LinkedList<*>
            assertEquals(incomeBox[0], certificateRequest)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun receiveRequestTest_FridayLaborBook_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val fridayClock = Clock.fixed(Instant.parse("2022-11-11T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = fridayClock
        assertThrows<NotAllowReceiveRequestException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_SaturdayNDFL_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val saturdayClock = Clock.fixed(Instant.parse("2022-11-12T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = saturdayClock
        assertThrows<WeekendDayException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_SaturdayLaborBook_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val saturdayClock = Clock.fixed(Instant.parse("2022-11-12T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = saturdayClock
        assertThrows<WeekendDayException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_SundayNDFL_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val sandayClock = Clock.fixed(Instant.parse("2022-11-13T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        hrDepartment.clock = sandayClock
        assertThrows<WeekendDayException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun receiveRequestTest_SundayLaborBook_throwException() {
        //setup
        val hrDepartment = HrDepartment
        val sandayClock = Clock.fixed(Instant.parse("2022-11-13T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        hrDepartment.clock = sandayClock
        assertThrows<WeekendDayException>{ hrDepartment.receiveRequest(certificateRequest) }
    }

    @Test
    fun processNextRequestTest_NDFL_OutcomeBoxIncreased(){
        //setup
        val hrDepartment = HrDepartment
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 5000L
        val byteArray = Random.nextBytes(100)
        every { Random.nextBytes(100) } returns byteArray
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, NDFL)
        val mondayClock = Clock.fixed(Instant.parse("2022-11-07T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        hrDepartment.clock = mondayClock
        hrDepartment.receiveRequest(certificateRequest)
        hrDepartment.processNextRequest(hrEmployeeNumber = TEST_HR_EMPLOYEE_NUMBER)
        val outcomeBox: LinkedList<*>

        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("outcomeOutcome")
            field.isAccessible = true
            outcomeBox = field.get(hrDepartment) as LinkedList<*>
            //проверка, что не кидает NPE
            assertDoesNotThrow{ outcomeBox.poll()}
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    @Test
    fun processNextRequestTest_LabourBook_OutcomeBoxIncreased(){
        //setup
        val hrDepartment = HrDepartment
        mockkObject(Random)
        every { Random.nextLong(5000L, 15000L) } returns 5000L
        val byteArray = Random.nextBytes(100)
        every { Random.nextBytes(100) } returns byteArray
        val certificateRequest = CertificateRequest(employeeNumber = TEST_EMPLOYEE_NUMBER, LABOUR_BOOK)
        val thursdayClock = Clock.fixed(Instant.parse("2022-11-08T00:01:00.00Z"), java.time.ZoneId.of("Europe/Moscow"))
        hrDepartment.clock = thursdayClock
        hrDepartment.receiveRequest(certificateRequest)
        hrDepartment.processNextRequest(hrEmployeeNumber = TEST_HR_EMPLOYEE_NUMBER)
        val outcomeBox: LinkedList<*>

        //assert
        try {
            val field = hrDepartment::class.java.getDeclaredField("outcomeOutcome")
            field.isAccessible = true
            outcomeBox = field.get(hrDepartment) as LinkedList<*>
            //проверка, что не кидает NPE
            assertDoesNotThrow{ outcomeBox.poll()}
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}