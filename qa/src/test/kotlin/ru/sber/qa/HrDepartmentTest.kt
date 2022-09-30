package ru.sber.qa

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.*
import java.lang.NullPointerException
import java.time.*
import kotlin.random.Random

internal class HrDepartmentTest {

    @BeforeEach
    fun setup() {
        mockkObject(Random)
        mockkStatic(LocalDateTime::class)
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestThrowsOnSaturday() {
        // Arrange
        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.values().random()
        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } returns DayOfWeek.SATURDAY

        // Act & Assert
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }
    }

    @Test
    fun receiveRequestThrowsOnSunday() {
        // Arrange
        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.values().random()
        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } returns DayOfWeek.SUNDAY

        // Act & Assert
        assertThrows<WeekendDayException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }
    }

    @Test
    fun receiveRequestNDFL() {
        // Arrange
        var iterator = 0
        val workdays = DayOfWeek.values().dropLast(2)

        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.NDFL

        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } answers {
            workdays.elementAt(iterator++)
        }

        // Act & Assert
        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }
    }

    @Test
    fun receiveRequestLabourBook() {
        // Arrange
        var iterator = 0
        val workdays = DayOfWeek.values().dropLast(2)

        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.LABOUR_BOOK

        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } answers {
            workdays.elementAt(iterator++)
        }

        // Act & Assert
        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertDoesNotThrow {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        assertThrows<NotAllowReceiveRequestException> {
            HrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }
    }

    @Test
    fun processNextRequestSuccess() {
        // Arrange
        val repeatTimes = Random.nextInt(1, 10)
        val hrDepartment = HrDepartment
        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.NDFL

        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } returns DayOfWeek.MONDAY
        every {
            Random.nextLong(any(), any())
        } returns 0

        // Act
        repeat(repeatTimes) {
            hrDepartment.receiveRequest(
                CertificateRequest(employeeNumber, certificateType)
            )
        }

        // Assert
        repeat(repeatTimes) {
            assertDoesNotThrow {
               hrDepartment.processNextRequest(employeeNumber)
            }
        }
    }

    @Test
    fun processNextRequestThrowsTimeout() {
        // Arrange
        val hrDepartment = HrDepartment
        val employeeNumber = Random.nextLong()
        val certificateType = CertificateType.NDFL

        every {
            LocalDateTime.now(any<Clock>()).dayOfWeek
        } returns DayOfWeek.MONDAY
        every {
            Random.nextLong(any(), any())
        } returns Scanner.SCAN_TIMEOUT_THRESHOLD + 1

        // Act
        hrDepartment.receiveRequest(
            CertificateRequest(employeeNumber, certificateType)
        )

        // Assert
        assertThrows<ScanTimeoutException> {
            hrDepartment.processNextRequest(employeeNumber)
        }
    }

    @Test
    fun processNextRequestThrowsOnEmptyIncome() {
        // Arrange
        val employeeNumber = Random.nextLong()

        // Act & Assert
        assertThrows<NullPointerException> {
            HrDepartment.processNextRequest(employeeNumber)
        }
    }
}