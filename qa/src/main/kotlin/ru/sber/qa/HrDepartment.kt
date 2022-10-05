package ru.sber.qa

import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.LinkedList

/**
 * Отдел кадров.
 */
object HrDepartment {
    var clock = Clock.systemUTC()
    // Не понимаю как обратиться к ним не меняя модификатор с private
    // пробовал создать локально свой Linked List и подсовывать его примерно вот так:
    // every { hrDepartment getProperty "incomeBox" } answers { localIn }
    // Но не пошло
    public val incomeBox: LinkedList<CertificateRequest> = LinkedList()
    public val outcomeOutcome: LinkedList<Certificate> = LinkedList()

    /**
     * Получение запроса на изготовление справки.
     *
     */
    fun receiveRequest(certificateRequest: CertificateRequest) {
        val currentDayOfWeek = LocalDateTime.now(clock).dayOfWeek
        if (currentDayOfWeek in listOf(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY))
            throw WeekendDayException()
        val isAllowReceiveRequest = when (certificateRequest.certificateType) {
            CertificateType.NDFL -> currentDayOfWeek in
                    listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
            CertificateType.LABOUR_BOOK -> currentDayOfWeek in
                    listOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
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
        outcomeOutcome.push(certificate)
    }
}
