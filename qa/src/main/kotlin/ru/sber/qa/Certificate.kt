package ru.sber.qa

/**
 * Готовая справка.
 */
class Certificate(
    val certificateRequest: CertificateRequest,
    val processedBy: Long,
    val data: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Certificate

        if (certificateRequest != other.certificateRequest) return false
        if (processedBy != other.processedBy) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = certificateRequest.hashCode()
        result = 31 * result + processedBy.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
