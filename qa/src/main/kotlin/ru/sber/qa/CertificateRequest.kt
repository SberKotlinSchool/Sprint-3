package ru.sber.qa

/**
 * Запрос на изготовление справки.
 */
class CertificateRequest(
    val employeeNumber: Long,
    val certificateType: CertificateType,
) {

    fun process(hrEmployeeNumber: Long, scanner: Scanner): Certificate =
        Certificate(certificateRequest = this, processedBy = hrEmployeeNumber, data = scanner.getScanData())
}
