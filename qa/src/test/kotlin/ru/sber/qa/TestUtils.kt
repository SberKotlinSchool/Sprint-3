package ru.sber.qa

import java.time.*

fun <T> getPrivateFiled(any: Any, fieldName: String): T {
    return any.javaClass.getDeclaredField(fieldName).let {
        it.isAccessible = true
        return@let it.get(any)
    } as T
}

fun getWednesdayClock(): Clock {
    val ldt = LocalDateTime.of(2022, Month.OCTOBER, 26, 0, 0, 0)
    return Clock.fixed(ldt.toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Moscow"))
}

fun getTuesdayClock(): Clock {
    val ldt = LocalDateTime.of(2022, Month.OCTOBER, 25, 0, 0, 0)
    return Clock.fixed(ldt.toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Moscow"))
}

fun getWeekendClock(): Clock? {
    val ldt = LocalDateTime.of(2022, Month.OCTOBER, 30, 0, 0, 0)
    return Clock.fixed(ldt.toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Moscow"))
}



