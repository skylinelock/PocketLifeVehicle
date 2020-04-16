package dev.sky_lock.pocketlifevehicle.gui

import dev.sky_lock.pocketlifevehicle.vehicle.model.*
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelBuilder.Companion.of
import org.bukkit.ChatColor
import org.bukkit.Material
import java.util.*

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
    var collideBaseSide = 0f
    var collideHeight = 0f
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
        return of(id!!)
                .name(name!!)
                .lore(lore!!)
                .spec(Spec(maxFuel, maxSpeed!!, capacity!!, SteeringLevel.NORMAL))
                .item(ItemOption(itemType!!, itemId, position!!))
                .collideBox(collideBaseSide, collideHeight)
                .big(isBig)
                .height(height)
                .sound(Sound.NONE)
                .build()
    }

    fun verifyCompleted(): Boolean {
        return id != null && name != null && maxFuel != 0.0f && maxSpeed != null && capacity != null &&  //steeringLevel != null &&
                itemType != null && itemId != 0 && position != null && height != 0.0f
        //sound != null;
    }

    fun unfilledOptionWarning(): List<String> {
        val lore: MutableList<String> = ArrayList()
        lore.add(ChatColor.RED.toString() + "設定が完了していません")
        lore.add(ChatColor.RED.toString() + "必須項目")
        if (id == null) {
            lore.add(ChatColor.RED.toString() + "- ID")
        }
        if (name == null) {
            lore.add(ChatColor.RED.toString() + "- 名前")
        }
        if (maxFuel == 0.0f) {
            lore.add(ChatColor.RED.toString() + "- 燃料上限")
        }
        if (maxSpeed == null) {
            lore.add(ChatColor.RED.toString() + "- 最高速度")
        }
        if (capacity == null) {
            lore.add(ChatColor.RED.toString() + "- 乗車人数")
        }
        //        if (steeringLevel == null) {
//            lore.add(ChatColor.RED + "- ステアリング感度");
//        }
        if (itemType == null || itemId == 0) {
            lore.add(ChatColor.RED.toString() + "- アイテム")
        }
        if (position == null) {
            lore.add(ChatColor.RED.toString() + "- アイテム位置")
        }
        if (height == 0.0f) {
            lore.add(ChatColor.RED.toString() + "- 座高")
        }
        //        if (sound == null) {
//            lore.add(ChatColor.RED + "- サウンド");
//        }
        return lore
    }
}