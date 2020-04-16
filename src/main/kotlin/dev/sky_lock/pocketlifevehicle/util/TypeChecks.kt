package dev.sky_lock.pocketlifevehicle.util

import java.util.*
import java.util.stream.Collectors

/**
 * @author sky_lock
 */
object TypeChecks {
    fun <E> checkListTypeDynamically(listObj: Any, type: Class<E>): List<E> {
        return Optional.of(listObj)
                .filter { obj: Any? -> MutableList::class.java.isInstance(obj) }
                .map<List<*>> { obj: Any? -> MutableList::class.java.cast(obj) }
                .map { list: List<*> -> checkTypeDynamically(list, type) }
                .orElseThrow { ClassCastException() }
    }

    private fun <E> checkTypeDynamically(uncheckedList: List<*>, type: Class<E>): List<E> {
        return uncheckedList.stream()
                .filter { obj: Any? -> type.isInstance(obj) }
                .map { obj: Any? -> type.cast(obj) }
                .collect(Collectors.toList())
    }
}