package ru.sber.qa

import java.util.*

object HrDepartmentExchange {

    private val incomeBox: LinkedList<CertificateRequest> = LinkedList()
    private val outcomeOutcome: LinkedList<Certificate> = LinkedList()

    fun pushIncome(certificateRequest: CertificateRequest) {
        incomeBox.push(certificateRequest)
    }

    fun pollIncomeBox(): CertificateRequest {
        return incomeBox.poll()
    }

    fun pushOutcome(certificate: Certificate) {
        outcomeOutcome.push(certificate)
    }

}