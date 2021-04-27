package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

/**
 * @author sky_lock
 */
@SerializableAs("Size")
class Size(var baseSide: Float, var height: Float) : ConfigurationSerializable {

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