package ru.sber.qa

/**
 * Готовая справка.
 */
data class Certificate(
    val certificateRequest: CertificateRequest,
    val processedBy: Long,
    val data: ByteArray,
) {

}
