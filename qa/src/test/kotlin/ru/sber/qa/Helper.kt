package ru.sber.qa

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

fun getClock(time: String): Clock {
    return Clock.fixed(Instant.parse(time), ZoneId.of("UTC"))
}