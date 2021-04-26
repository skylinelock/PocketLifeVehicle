package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

/**
 * @author sky_lock
 */

@SerializableAs("SeatOption")
class SeatOption(var capacity: Capacity, var offset: Float, var depth: Float, var width: Float): ConfigurationSerializable {

    override fun serialize(): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["capacity"] = capacity.value()
        map["offset"] = offset
        map["depth"] = depth
        map["width"] = width
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): SeatOption {
            val capacity = Capacity.valueOf(map["capacity"] as Int)
            val offset = (map["offset"] as Double).toFloat()
            val depth = (map["depth"] as Double).toFloat()
            val width = (map["width"] as Double).toFloat()
            return SeatOption(capacity, offset, depth, width)
        }
    }
}