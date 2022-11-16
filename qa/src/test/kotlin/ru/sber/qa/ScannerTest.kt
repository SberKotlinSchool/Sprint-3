package ru.sber.qa

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Scanner
import kotlin.test.assertFailsWith


internal class ScannerTest {

    @Test
    fun getScanData() {
        val certificateRequest = CertificateRequest(1, CertificateType.LABOUR_BOOK)
        val process = certificateRequest.process(1)
        assertNotNull(process.data)
    }

    @Test
    fun shouldGetWeekendDayException() {
        assertFailsWith<ScanTimeoutException>("Не получено корректное исключение ScanTimeoutException") {
           val scanner = Scanner
            scanner.getScanData(1000L)
        }
    }
}