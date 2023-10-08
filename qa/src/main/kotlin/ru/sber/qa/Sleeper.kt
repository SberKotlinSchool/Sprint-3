package ru.sber.qa

interface Sleeper {
    fun sleep(duration: Long)
}

class ThreadSleeper : Sleeper {

    override fun sleep(duration: Long) {
        Thread.sleep(duration)
    }
}