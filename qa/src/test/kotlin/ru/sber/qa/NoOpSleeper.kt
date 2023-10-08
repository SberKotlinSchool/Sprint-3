package ru.sber.qa

class NoOpSleeper : Sleeper {
    override fun sleep(duration: Long) {
        // no-op
    }
}