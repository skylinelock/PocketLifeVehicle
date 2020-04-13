package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.Material
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import java.util.*

/**
 * @author sky_lock
 */
@SerializableAs("ItemOption")
class ItemOption(val type: Material, val id: Int) : ConfigurationSerializable {
    var position = ItemPosition.HEAD

    constructor(type: Material, id: Int, position: ItemPosition) : this(type, id) {
        this.position = position
    }

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["type"] = type.name
        map["id"] = id
        map["position"] = position.toString()
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): ItemOption {
            val type = Material.valueOf((map["type"] as String))
            val id = map["id"].toString().toInt()
            val position = ItemPosition.valueOf(map["position"].toString())
            return ItemOption(type, id, position)
        }
    }

}