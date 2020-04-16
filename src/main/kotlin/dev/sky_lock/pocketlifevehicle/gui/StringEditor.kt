package dev.sky_lock.pocketlifevehicle.gui

import net.minecraft.server.v1_14_R1.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryPlayer
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * @author sky_lock
 */
class StringEditor private constructor(windowId: Int, editorType: Type, playerinventory: PlayerInventory, world: World) : ContainerAnvil(windowId, playerinventory, ContainerAccess.at(world, BlockPosition.ZERO)) {
    val editorType: Type
    var menu: ModelSettingMenu? = null

    override fun e() {
        super.e()
        levelCost.set(0)
    }

    enum class Type(val label: String) {
        ID("ID"), NAME("名前"), HEIGHT("座高");
    }

    companion object {
        private val openingMaps: MutableMap<UUID, StringEditor> = HashMap()
        fun open(player: Player, editorType: Type, menu: ModelSettingMenu) {
            val inventory = (player.inventory as CraftInventoryPlayer).inventory
            val world: World = (player.world as CraftWorld).handle
            val entityPlayer = (player as CraftPlayer).handle
            val containerId = entityPlayer.nextContainerCounter()
            val editor = StringEditor(containerId, editorType, inventory, world)
            editor.menu = menu
            entityPlayer.playerConnection.sendPacket(PacketPlayOutOpenWindow(containerId, Containers.ANVIL, ChatMessage("文字を編集する")))
            entityPlayer.activeContainer = editor
            entityPlayer.activeContainer.addSlotListener(entityPlayer)
            val itemStack = ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)
            val meta = Objects.requireNonNull(itemStack.itemMeta)
            meta.setDisplayName(editorType.label)
            itemStack.itemMeta = meta
            val item = CraftItemStack.asNMSCopy(itemStack)
            val nbt = item.orCreateTag
            nbt.setBoolean("editor-result", true)
            /*        NBTTagCompound displayNBT = new NBTTagCompound();
        displayNBT.setString("Name", editor.getEditorType().getName());
        nbt.set("display", displayNBT);*/
            item.tag = nbt
            editor.bukkitView.topInventory.setItem(0, CraftItemStack.asBukkitCopy(item))
            openingMaps[player.getUniqueId()] = editor
        }

        operator fun get(player: Player): StringEditor? {
            return openingMaps[player.uniqueId]
        }

        fun close(player: Player) {
            if (!openingMaps.containsKey(player.uniqueId)) {
                return
            }
            openingMaps[player.uniqueId]!!.bukkitView.topInventory.clear()
            openingMaps.remove(player.uniqueId)
        }

        fun isOpening(player: Player): Boolean {
            return openingMaps.keys.contains(player.uniqueId)
        }
    }

    init {
        checkReachable = false
        this.editorType = editorType
    }
}