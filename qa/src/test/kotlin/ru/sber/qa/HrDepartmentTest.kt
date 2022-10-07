package ru.sber.qa

import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.assertFailsWith

internal class HrDepartmentTest {

    private val employeeId = abs(Random.nextLong())
    private lateinit var certificateRequest: CertificateRequest

    @BeforeEach
    fun init() {
        certificateRequest = spyk(CertificateRequest(employeeId, CertificateType.NDFL))
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun receiveRequestTest() {

    }

    @Test
    fun processNextRequestTest() {

        val hrDepartment = HrDepartment
        val data = Random.nextBytes(100)
        val certificate = Certificate(certificateRequest, employeeId, data)

        assertFailsWith<NullPointerException> { hrDepartment.processNextRequest(employeeId) }

        every { certificateRequest.process(employeeId) } returns certificate

        hrDepartment.receiveRequest(certificateRequest)
        hrDepartment.processNextRequest(employeeId)

//        assert(hrDepartment.outcomeOutcome.size == 1)
//        assert(hrDepartment.incomeBox.size == 0)

    }
}