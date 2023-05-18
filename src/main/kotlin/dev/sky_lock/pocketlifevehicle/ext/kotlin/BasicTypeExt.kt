package dev.sky_lock.pocketlifevehicle.ext.kotlin

/**
 * @author sky_lock
 */

fun Float.truncateToOneDecimalPlace(): String {
    return String.format("%.1f", this)
}