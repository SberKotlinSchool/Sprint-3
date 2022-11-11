package ru.sber.qa.component

import ru.sber.qa.exception.NotAllowReceiveRequestException
import ru.sber.qa.exception.WeekendDayException
import ru.sber.qa.model.Certificate
import ru.sber.qa.model.CertificateRequest
import ru.sber.qa.model.CertificateType
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

/**
 * Отдел кадров.
 */
object HrDepartment {
    var clock = Clock.systemUTC()
    private val incomeBox: LinkedList<CertificateRequest> = LinkedList()
    private val outcomeBox: LinkedList<Certificate> = LinkedList()

    /**
     * Получение запроса на изготовление справки.
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
        incomeBox.push(certificateRequest)
    }

    /**
     * Обработка запросов в порядке поступления.
     */
    fun processNextRequest(hrEmployeeNumber: Long) {
        val certificateRequest = incomeBox.poll()
        val certificate = certificateRequest.process(hrEmployeeNumber)
        outcomeBox.push(certificate)
    }
}
