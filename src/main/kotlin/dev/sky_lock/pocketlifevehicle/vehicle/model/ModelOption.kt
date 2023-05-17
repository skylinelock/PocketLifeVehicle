package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

/**
 * @author sky_lock
 */

@SerializableAs("ModelOption")
class ModelOption(var type: Material, var id: Int, var position: ItemPosition, var isBig: Boolean): ConfigurationSerializable {

    fun bigText(): String {
        return if (isBig) "大きい" else "小さい"
    }

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["type"] = type.name
        map["id"] = id
        map["position"] = position.toString()
        map["big"] = isBig
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): ModelOption {
            val type = Material.valueOf((map["type"] as String))
            val id = map["id"].toString().toInt()
            val position = ItemPosition.valueOf(map["position"].toString())
            val isBig = map["big"].toString().toBoolean()
            return ModelOption(type, id, position, isBig)
        }
    }
}