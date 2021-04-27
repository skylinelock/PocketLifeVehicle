package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.minecraft.server.v1_14_R1.*
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryAnvil
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryView
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * @author sky_lock
 */

class ContainerModelTextEdit constructor(
    title: String,
    default: String,
    private val modifyType: ModifyType,
    val model: Model?,
    val player: Player
) : ContainerAnvil(
    (player as CraftPlayer).handle.nextContainerCounter(),
    (player.inventory as CraftInventoryPlayer).inventory,
    ContainerAccess.at((player.world as CraftWorld).handle, BlockPosition.ZERO)
) {

    init {
        checkReachable = false
        setTitle(ChatMessage(title))

        val paper = ItemStackBuilder(Material.PAPER, 1)
            .setName(default.ifBlank { "name" })
            .setPersistentData(
                VehiclePlugin.instance.createKey("editor-result"),
                PersistentDataType.SHORT,
                1
            )
            .build()

        bukkitView.topInventory.setItem(0, paper)
    }

    override fun getBukkitView(): CraftInventoryView {
        val inventory = super.getBukkitView().topInventory as CraftInventoryAnvil
        val location = inventory.location ?: player.location
        val customInventory = CraftModelTextEditor(location, inventory.inventory, inventory.resultInventory, this)
        return CraftInventoryView(player, customInventory, this);
    }

    // update
    override fun e() {
        super.e()
        levelCost.set(0)
    }

    inner class CraftModelTextEditor(
        location: Location, inventory: IInventory, inventory2: IInventory, private val container: ContainerModelTextEdit
    ) : CraftInventoryAnvil(location, inventory, inventory2, container) {

        fun onClick(event: InventoryClickEvent) {
            if (event.slotType == InventoryType.SlotType.OUTSIDE) {
                return
            }
            if (event.click.isShiftClick) {
                event.isCancelled = true
                return
            }
            // クリックしたインベントリーがBottomInventoryか
            if (event.clickedInventory == event.view.bottomInventory) {
                return
            }
            // クリックしたインベントリーがTopInventoryか
            if (event.inventory != event.clickedInventory) {
                return
            }
            if (event.action == InventoryAction.HOTBAR_SWAP || event.action == InventoryAction.HOTBAR_MOVE_AND_READD) {
                event.isCancelled = true
                return
            }
            if (!(event.click.isRightClick || event.click.isLeftClick)) {
                event.isCancelled = true
                return
            }
            event.isCancelled = true
            event.result = Event.Result.DENY

            val text = renameText
            if (text == null || text.isBlank()) {
                return
            }
            val current = event.currentItem ?: return
            val model = model ?: ModelRegistry.default(text)
            val slot = event.slot
            if (slot == 2) {
                when (modifyType) {
                    ModifyType.ID_CREATE, ModifyType.ID -> {
                        if (ModelRegistry.hasRegistered(text)) {
                            displayError(current, ChatColor.RED + "そのIDは既に登録されています。")
                            return
                        }
                        ModelRegistry.register(model)
                    }
                    ModifyType.HEIGHT -> {
                        val height = text.toFloatOrNull()
                        if (height == null) {
                            displayError(current, ChatColor.RED + "有効な数字を入力して下さい")
                            return
                        }
                        model.height = text.toFloat()
                    }
                    ModifyType.NAME -> {
                        model.name = ChatColor.translateAlternateColorCodes('&', text)
                    }
                }
                event.view.topInventory.clear(0)
                player.openInventory(InventoryModelOption(player, model))
                return
            }
            if (slot != 0) {
                return
            }
            event.view.topInventory.clear(0)
            when (modifyType) {
                ModifyType.ID_CREATE -> {
                    player.openInventory(InventoryModelList(player))
                }
                else -> {
                    player.openInventory(InventoryModelOption(player, model))
                }
            }

        }

        fun displayError(itemStack: ItemStack, error: String) {
            val meta = itemStack.itemMeta
            meta.lore = listOf(error)
            itemStack.itemMeta = meta
        }

        fun onClose(event: InventoryCloseEvent) {
            event.view.topInventory.clear(0)
        }
    }

    enum class ModifyType {
        ID_CREATE, ID, NAME, HEIGHT
    }

}