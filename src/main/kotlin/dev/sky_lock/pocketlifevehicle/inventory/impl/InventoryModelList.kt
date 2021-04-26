package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.inventory.InventoryCustom
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.ArrayList

/**
 * @author sky_lock
 */

class InventoryModelList(private val player: Player): InventoryCustom(27, "モデル一覧") {
    private var page = 1
    private val models = ModelRegistry.set().toList()

    init {
        val addVehicleCart = ItemStackBuilder(Material.CHEST_MINECART, 1).setName(ChatColor.GREEN + "車種を追加する").build()
        setSlot(22, addVehicleCart) { event ->
        }
        setModelSlots()
    }

    private fun clearModelSlots() {
        for (i in 0..17) {
            clear(i)
        }
        clear(21)
        clear(23)
    }

    private fun setModelSlots() {
        val start = (page - 1) * 18
        val end = page * 18 - 1
        var count = 0
        for (model in models) {
            if (count < start) {
                count++
                continue
            }
            if (count > end) {
                val next = ItemStackBuilder(Material.IRON_NUGGET, 1).setName(ChatColor.AQUA + "次のページ").setCustomModelData(16).build()
                setSlot(23, next) { event ->
                    page++
                    clearModelSlots()
                    setModelSlots()
                }
                break
            }
            setSlot(count % 18, modelItem(model)) { event ->
                player.openInventory(InventoryModelOption(player, model))
            }
            count++
        }

        if (page > 1) {
            val back =
                ItemStackBuilder(Material.IRON_NUGGET, 1).setName(ChatColor.AQUA + "前のページ").setCustomModelData(17)
                    .build()
            setSlot(21, back) { event ->
                if (page > 1) page--
                clearModelSlots()
                setModelSlots()
            }
        }
    }

    private fun modelItem(model: Model): ItemStack {
        val desc: MutableList<String> = ArrayList()
        desc.add(ChatColor.DARK_AQUA + "名前: " + ChatColor.AQUA + model.name)
        //TODO: [] -> ""
        var lore: List<String?> = model.lore
        if (lore.isEmpty()) {
            lore = listOf("")
        }
        desc.add(ChatColor.DARK_AQUA + "説明: " + ChatColor.AQUA + lore)
        val spec = model.spec
        desc.add(ChatColor.DARK_AQUA + "燃料上限: " + ChatColor.AQUA + spec.maxFuel)
        desc.add(ChatColor.DARK_AQUA + "最高速度: " + ChatColor.AQUA + spec.maxSpeed.label)
        desc.add(ChatColor.DARK_AQUA + "乗車人数: " + ChatColor.AQUA + model.capacity.value())
        val itemOption = model.itemOption
        val box = model.size
        desc.add(ChatColor.DARK_AQUA + "モデル位置: " + ChatColor.AQUA + itemOption.position.label)
        desc.add(ChatColor.DARK_AQUA + "当たり判定(高さ): " + ChatColor.AQUA + box.height)
        desc.add(ChatColor.DARK_AQUA + "当たり判定(底辺): " + ChatColor.AQUA + box.baseSide)
        val size = if (model.isBig) "大きい" else "小さい"
        desc.add(ChatColor.DARK_AQUA + "大きさ: " + ChatColor.AQUA + size)
        desc.add(ChatColor.DARK_AQUA + "座高: " + ChatColor.AQUA + model.height)
        return ItemStackBuilder(model.itemStack).setName(ChatColor.YELLOW + model.id).setLore(desc).build()
    }
}