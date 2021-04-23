package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import java.util.*

/**
 * @author sky_lock
 */
@SerializableAs("Size")
class Size(val baseSide: Float, val height: Float) : ConfigurationSerializable {

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["baseside"] = baseSide
        map["height"] = height
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Size {
            val baseSide = (map["baseside"] as Double).toFloat()
            val height = (map["height"] as Double).toFloat()
            return Size(baseSide, height)
        }
    }

}