package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import java.util.HashMap

/**
 * @author sky_lock
 */

@SerializableAs("Position")
class Position(val offset: Float, val depth: Float, val width: Float): ConfigurationSerializable {

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["offset"] = offset
        map["depth"] = depth
        map["width"] = width
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Position {
            val offset = (map["offset"] as Double).toFloat()
            val depth = (map["depth"] as Double).toFloat()
            val width = (map["width"] as Double).toFloat()
            return Position(offset, depth, width)
        }
    }
}