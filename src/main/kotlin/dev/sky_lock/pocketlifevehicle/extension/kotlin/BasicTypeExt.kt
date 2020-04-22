package dev.sky_lock.pocketlifevehicle.extension.kotlin

/**
 * @author sky_lock
 */

fun Float.truncateToOneDecimalPlace(): String {
    return String.format("%.1f", this)
}

fun String.removeWhiteSpace(): String {
    return this.replace("\\s".toRegex(), "")
}