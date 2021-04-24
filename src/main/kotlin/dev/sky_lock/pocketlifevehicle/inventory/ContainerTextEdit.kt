package dev.sky_lock.pocketlifevehicle.inventory

import dev.sky_lock.pocketlifevehicle.gui.EditSessions
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import net.minecraft.server.v1_14_R1.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author sky_lock
 */

class ContainerTextEdit constructor(windowId: Int, inventory: PlayerInventory, world: World, default: String)
    : ContainerAnvil(windowId, inventory, ContainerAccess.at(world, BlockPosition.ZERO)) {

    init {
        checkReachable = false
        title = ChatMessage("数値・文字エディタ")

        val itemStack = ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)
        val meta = Objects.requireNonNull(itemStack.itemMeta)
        meta.setDisplayName(default)
        itemStack.itemMeta = meta

        val item = CraftItemStack.asNMSCopy(itemStack)
        val nbt = item.orCreateTag
        nbt.setBoolean("editor-result", true)
        item.tag = nbt

        bukkitView.topInventory.setItem(0, CraftItemStack.asBukkitCopy(item))
    }

    override fun e() {
        super.e()
        levelCost.set(0)
    }

}