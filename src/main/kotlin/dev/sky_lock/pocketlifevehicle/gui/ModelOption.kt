package dev.sky_lock.pocketlifevehicle.gui

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.vehicle.model.*
import org.bukkit.ChatColor
import org.bukkit.Material

/**
 * @author sky_lock
 */
class ModelOption {
    var isJustEditing = false
    var id: String? = null
    var name: String? = null
    var lore: List<String>? = null
    var maxSpeed: MaxSpeed? = null
    var maxFuel = 0f
    var capacity: Capacity? = null
    var steeringLevel: SteeringLevel? = null
    var itemType: Material? = null
    var itemId = 0
        private set
    var position: ItemPosition? = null
        private set
    var collideBaseSide = 0.2f
    var collideHeight = 0.2f
    var isBig = false
    var height = 0f
    var sound: Sound? = null

    fun setItemID(itemId: Int) {
        this.itemId = itemId
    }

    fun setItemPosition(position: ItemPosition?) {
        this.position = position
    }

    val isItemValid: Boolean
        get() = itemType != null && itemId != 0

    fun generate(): Model {
        val lore = this.lore ?: ArrayList()
        return Model(
                id = id!!,
                name = name!!,
                lore = lore,
                spec = Spec(maxFuel, maxSpeed!!, capacity!!, SteeringLevel.NORMAL),
                itemOption = ItemOption(itemType!!, itemId, position!!),
                collideBox = CollideBox(collideBaseSide, collideHeight),
                isBig = isBig,
                height = height,
                sound = Sound.NONE
        )
    }

    fun verifyCompleted(): Boolean {
        return id != null && name != null &&
                maxFuel != 0.0f && maxSpeed != null && capacity != null &&
                itemType != null && itemId != 0 &&
                position != null && height != 0.0f
    }

    fun unfilledOptionWarning(): List<String> {
        val lore: MutableList<String> = ArrayList()
        lore.add(ChatColor.RED + "設定が完了していません")
        lore.add(ChatColor.RED + "必須項目")
        if (id == null) {
            lore.add(ChatColor.RED + "- ID")
        }
        if (name == null) {
            lore.add(ChatColor.RED + "- 名前")
        }
        if (maxFuel == 0.0f) {
            lore.add(ChatColor.RED + "- 燃料上限")
        }
        if (maxSpeed == null) {
            lore.add(ChatColor.RED + "- 最高速度")
        }
        if (capacity == null) {
            lore.add(ChatColor.RED + "- 乗車人数")
        }
        //        if (steeringLevel == null) {
//            lore.add(ChatColor.RED + "- ステアリング感度");
//        }
        if (itemType == null || itemId == 0) {
            lore.add(ChatColor.RED + "- アイテム")
        }
        if (position == null) {
            lore.add(ChatColor.RED + "- アイテム位置")
        }
        if (height == 0.0f) {
            lore.add(ChatColor.RED + "- 座高")
        }
        //        if (sound == null) {
//            lore.add(ChatColor.RED + "- サウンド");
//        }
        return lore
    }
}