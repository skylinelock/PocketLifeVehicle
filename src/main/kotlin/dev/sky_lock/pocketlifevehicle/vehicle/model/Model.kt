package dev.sky_lock.pocketlifevehicle.vehicle.model

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author sky_lock
 */
@SerializableAs("Model")
class Model constructor(val id: String, val name: String,
                        val lore: List<String>, val spec: Spec, val itemOption: ItemOption,
                        val size: Size, val isBig: Boolean, val height: Float, val sound: Sound,
                        val capacity: Capacity,
                        val position: Position) : ConfigurationSerializable {

    val itemStack: ItemStack
        get() {
            return ItemStackBuilder(itemOption.type, 1)
                    .setName(name)
                    .setLore(*lore.map { text -> ChatColor.RESET + text }.toTypedArray())
                    .setCustomModelData(itemOption.id)
                    .setUnbreakable(true)
                    .addItemFlags(*ItemFlag.values())
                    .build()
        }

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["id"] = id
        map["name"] = name
        map["lore"] = lore
        map["spec"] = spec
        map["item"] = itemOption
        map["size"] = size
        map["big"] = isBig
        map["height"] = height
        map["sound"] = sound.toString()
        map["capacity"] = capacity.value()
        map["position"] = position
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Model {
            val id = map["id"].toString()
            val name = map["name"].toString()
            val lore = (map["lore"] as List<*>).filterIsInstance<String>()
            val spec = map["spec"] as Spec
            val itemOption = map["item"] as ItemOption
            val size = map["size"] as Size
            val isBig = map["big"].toString().toBoolean()
            val height = (map["height"] as Double).toFloat()
            val sound = Sound.valueOf(map["sound"].toString())
            val capacity = Capacity.valueOf(map["capacity"] as Int)
            val position = map["position"] as Position
            return Model(
                    id = id,
                    name = name,
                    lore = lore,
                    spec = spec,
                    itemOption = itemOption,
                    size = size,
                    isBig = isBig,
                    height = height,
                    sound = sound,
                    capacity = capacity,
                    position = position
            )
        }
    }
}