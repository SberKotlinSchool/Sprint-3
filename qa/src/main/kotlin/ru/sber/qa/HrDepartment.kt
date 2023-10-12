package ru.sber.qa

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

/**
 * Отдел кадров.
 */
object HrDepartment {
    var clock = Clock.systemUTC()
    private val hrDepartmentExchange = HrDepartmentExchange


    /**
     * Получение запроса на изготовление справки.
     *
     */
    fun receiveRequest(certificateRequest: CertificateRequest) {
        val currentDayOfWeek = LocalDateTime.now(clock).dayOfWeek
        if (currentDayOfWeek in listOf(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY))
            throw WeekendDayException()
        val isAllowReceiveRequest = when (certificateRequest.certificateType) {
            CertificateType.NDFL -> currentDayOfWeek in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
            CertificateType.LABOUR_BOOK -> currentDayOfWeek in listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
        }
        if (!isAllowReceiveRequest)
            throw NotAllowReceiveRequestException()
        hrDepartmentExchange.pushIncome(certificateRequest)
    }

    /**
     * Обработка запросов в порядке поступления.
     */
    fun processNextRequest(hrEmployeeNumber: Long) {
        val certificateRequest = hrDepartmentExchange.pollIncomeBox()
        val certificate = certificateRequest.process(hrEmployeeNumber)
        hrDepartmentExchange.pushOutcome(certificate)
    }
}
