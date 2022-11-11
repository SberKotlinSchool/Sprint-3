package ru.sber.qa

import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream

const val INCOME_BOX_FIELD_NAME = "incomeBox"
const val OUTCOME_BOX_FIELD_NAME = "outcomeBox"


fun <R> getField(instance: Any, propertyName: String): R {
    val field = instance.javaClass.getDeclaredField(propertyName)
    field.isAccessible = true
    return field.get(instance) as R
}

fun <R> addAllToLinkedListField(instance: Any, propertyName: String, value: LinkedList<R>) {
    val field = getField<LinkedList<R>>(instance, propertyName)
    value.forEach { field.add(it) }
}

fun <R> clearLinkedListField(instance: Any, propertyName: String) {
    val field = getField<LinkedList<R>>(instance, propertyName)
    field.clear()
}

fun getTestByteArray(): ByteArray {
    return IntStream.range(0, 100).mapToObj(Int::toByte).collect(Collectors.toList()).toByteArray()
}
