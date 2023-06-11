package dev.sky_lock.pocketlifevehicle.inventory.impl

import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryAnvil
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryPlayer
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventoryView
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

/**
 * @author sky_lock
 */

class ContainerModelTextEdit(
    title: String,
    default: String,
    private val modifyType: ModifyType,
    val model: Model?,
    // TODO: 変数削除
    val bukkitPlayer: Player
) : AnvilMenu(
    (bukkitPlayer as CraftPlayer).handle.nextContainerCounter(),
    (bukkitPlayer.inventory as CraftInventoryPlayer).inventory,
    ContainerLevelAccess.create((bukkitPlayer.world as CraftWorld).handle, BlockPos.ZERO)
) {

    init {
        checkReachable = false
        setTitle(Component.literal(title))

        val paper = ItemStackBuilder(Material.PAPER, 1)
            .setName(Line().raw(default))
            .build()

        bukkitView.topInventory.setItem(0, paper)
    }

    override fun getBukkitView(): CraftInventoryView {
        val inventory = super.getBukkitView().topInventory as CraftInventoryAnvil
        val location = inventory.location ?: bukkitPlayer.location
        val customInventory = CraftModelTextEditor(location, inventory.inventory, inventory.resultInventory, this)
        return CraftInventoryView(bukkitPlayer, customInventory, this)
    }

    override fun createResult() {
        super.createResult()
        cost.set(0)
    }

    inner class CraftModelTextEditor(
        location: Location, inventory: Container, inventory2: Container, container: ContainerModelTextEdit
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
            if (text.isNullOrBlank()) {
                return
            }
            val current = event.currentItem ?: return
            var model = model ?: ModelRegistry.default(text)
            val slot = event.slot
            if (slot == 2) {
                when (modifyType) {
                    ModifyType.ID_CREATE -> {
                        if (ModelRegistry.hasRegistered(text)) {
                            displayError(current, Line().red("そのIDは既に登録されています"))
                            return
                        }
                        ModelRegistry.register(model)
                    }
                    ModifyType.ID_RECREATE -> {
                        if (ModelRegistry.hasRegistered(text)) {
                            displayError(current, Line().red("そのIDは既に登録されています"))
                            return
                        }
                        ModelRegistry.unregister(model.id)
                        model = ModelRegistry.recreate(text, model)
                        ModelRegistry.register(model)
                    }
                    ModifyType.ID_COPY -> {
                        if (ModelRegistry.hasRegistered(text)) {
                            displayError(current, Line().red("そのIDは既に登録されています"))
                            return
                        }
                        model = ModelRegistry.recreate(text, model)
                        ModelRegistry.register(model)
                    }
                    ModifyType.NAME -> {
                        model.name = text
                    }
                    ModifyType.HEIGHT -> {
                        val height = text.toFloatOrNull()
                        if (height == null) {
                            displayError(current, Line().red("有効な数字を入力して下さい"))
                            return
                        }
                        model.height = text.toFloat()
                    }
                    ModifyType.OFFSET -> {
                        val offset = text.toFloatOrNull()
                        if (offset == null) {
                            displayError(current, Line().red("有効な数字を入力して下さい"))
                            return
                        }
                        model.seatOption.offset = offset
                    }
                    ModifyType.WIDTH -> {
                        val width = text.toFloatOrNull()
                        if (width == null) {
                            displayError(current, Line().red("有効な数字を入力して下さい"))
                            return
                        }
                        model.seatOption.width = width
                    }
                    ModifyType.DEPTH -> {
                        val depth = text.toFloatOrNull()
                        if (depth == null) {
                            displayError(current, Line().red("有効な数字を入力して下さい"))
                            return
                        }
                        model.seatOption.depth = text.toFloat()
                    }
                }
                event.view.topInventory.clear(0)
                bukkitPlayer.openInventory(InventoryModelOption(bukkitPlayer, model))
                return
            }
            if (slot != 0) {
                return
            }
            event.view.topInventory.clear(0)
            when (modifyType) {
                ModifyType.ID_CREATE -> {
                    bukkitPlayer.openInventory(InventoryModelList(bukkitPlayer))
                }
                ModifyType.WIDTH, ModifyType.DEPTH, ModifyType.OFFSET -> {
                    bukkitPlayer.openInventory(InventoryModelArmorStand(bukkitPlayer, model))
                }
                else -> {
                    bukkitPlayer.openInventory(InventoryModelOption(bukkitPlayer, model))
                }
            }

        }

        private fun displayError(itemStack: ItemStack, line: Line) {
            val meta = itemStack.itemMeta
            meta.lore(listOf(line.toComponent()))
            itemStack.itemMeta = meta
        }

        fun onClose(event: InventoryCloseEvent) {
            event.view.topInventory.clear(0)
        }
    }

    enum class ModifyType {
        ID_CREATE, ID_RECREATE, ID_COPY, NAME, HEIGHT, OFFSET, WIDTH, DEPTH
    }

}