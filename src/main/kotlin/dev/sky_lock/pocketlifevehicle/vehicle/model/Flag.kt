package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

/**
 * @author sky_lock
 */

@SerializableAs("Flag")
class Flag(var engineSound: Boolean, var animation: Boolean, var consumeFuel: Boolean, var eventOnly: Boolean): ConfigurationSerializable {

    fun engineSoundText(): String {
        return if (engineSound) "切替可能" else "なし"
    }

    fun animationText(): String {
        return if (animation) "切替可能" else "なし"
    }

    fun consumeFuelText(): String {
        return if (consumeFuel) "する" else "しない"
    }

    fun eventOnlyText(): String {
        return if (eventOnly) "はい" else "いいえ"
    }

    override fun serialize(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["engineSound"] = engineSound
        map["animation"] = animation
        map["consumeFuel"] = consumeFuel
        map["eventOnly"] = eventOnly
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Flag {
            val engineSound = map["engineSound"].toString().toBoolean()
            val animation = map["animation"].toString().toBoolean()
            val consumeFuel = map["consumeFuel"].toString().toBoolean()
            val eventOnly = map["eventOnly"].toString().toBoolean()
            return Flag(engineSound, animation, consumeFuel, eventOnly)
        }
    }
}