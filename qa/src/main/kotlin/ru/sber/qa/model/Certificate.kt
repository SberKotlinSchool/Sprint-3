package ru.sber.qa.model

/**
 * Готовая справка.
 */
class Certificate(
    val certificateRequest: CertificateRequest,
    val processedBy: Long,
    val data: ByteArray,
) {

}
