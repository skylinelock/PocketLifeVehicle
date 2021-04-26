package dev.sky_lock.pocketlifevehicle.vehicle.model

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */
@SerializableAs("Model")
class Model constructor(
    val id: String, var name: String,
    var lore: List<String>, val spec: Spec, val flag: Flag,
    val size: Size, var height: Float, var sound: Sound,
    val modelOption: ModelOption, val seatOption: SeatOption
) : ConfigurationSerializable {

    val itemStack: ItemStack
        get() {
            return ItemStackBuilder(modelOption.type, 1)
                .setName(name)
                .setLore(*lore.map { text -> ChatColor.RESET + text }.toTypedArray())
                .setCustomModelData(modelOption.id)
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
        map["flag"] = flag
        map["size"] = size
        map["height"] = height
        map["sound"] = sound.toString()
        map["modelOption"] = modelOption
        map["seatOption"] = seatOption
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Model {
            val id = map["id"].toString()
            val name = map["name"].toString()
            val lore = (map["lore"] as List<*>).filterIsInstance<String>()
            val spec = map["spec"] as Spec
            val flag = map["flag"] as Flag
            val size = map["size"] as Size
            val height = (map["height"] as Double).toFloat()
            val sound = Sound.valueOf(map["sound"].toString())
            val modelOption = map["modelOption"] as ModelOption
            val seatOption = map["seatOption"] as SeatOption
            return Model(
                id = id,
                name = name,
                lore = lore,
                spec = spec,
                flag = flag,
                size = size,
                height = height,
                sound = sound,
                modelOption = modelOption,
                seatOption = seatOption
            )
        }
    }
}