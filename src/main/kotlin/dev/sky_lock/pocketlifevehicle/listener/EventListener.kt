package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.click.CarClick
import dev.sky_lock.pocketlifevehicle.click.InventoryClick
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.removeWhiteSpace
import dev.sky_lock.pocketlifevehicle.gui.EditSessions
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.spawn
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities.tow
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftArmorStand
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.spigotmc.event.entity.EntityDismountEvent
import java.util.*

/**
 * @author sky_lock
 */
class EventListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) {
            return
        }
        val armorStand = event.rightClicked as CraftArmorStand
        if (armorStand.handle !is ModelArmorStand && armorStand.handle !is SeatArmorStand) {
            return
        }
        event.isCancelled = true
        CarClick(event.player, armorStand).accept()
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        InventoryClick(event).accept()
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        StringEditor.close(player)
        Bukkit.getScheduler().runTaskLater(PLVehicle.instance, Runnable {
            if (player.openInventory.topInventory.type == InventoryType.CRAFTING) {
                EditSessions.destroy(player.uniqueId)
            }
        }, 1L)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        val itemStack = event.item ?: return
        val model = Storage.MODEL.findByItemStack(itemStack) ?: return
        event.isCancelled = true
        event.setUseInteractedBlock(Event.Result.DENY)
        event.setUseItemInHand(Event.Result.DENY)
        val meta = itemStack.itemMeta
        val player = event.player
        if (!PLVehicle.instance.pluginConfiguration.getAllowWorlds().contains(event.player.world)) {
            player.sendActionBar(ChatColor.RED + "このワールドでは乗り物は使用できません")
            return
        }
        if (event.blockFace != BlockFace.UP) {
            player.sendActionBar(ChatColor.RED + "乗り物は地面にのみ設置できます")
            return
        }
        val whereToSpawn = Objects.requireNonNull(event.clickedBlock)!!.location.clone().add(0.5, 1.0, 0.5)
        if (!meta.hasLore()) {
            placeCarEntity(player, itemStack, event.hand, player.uniqueId, model, player.location, model.spec.maxFuel)
            return
        }
        val ownerUUID = meta.persistentDataContainer.get(PLVehicle.instance.createKey("owner"), PersistentDataType.STRING)
        if (ownerUUID == null) {
            placeCarEntity(player, itemStack, event.hand, player.uniqueId, model, player.location, model.spec.maxFuel)
            return
        }
        val owner = UUID.fromString(ownerUUID)
        val lore = Objects.requireNonNull(meta.lore)
        val rawFuel = lore!![1]
        var fuel = rawFuel.removeWhiteSpace().split(":".toRegex()).toTypedArray()[1].toFloat()
        if (fuel > model.spec.maxFuel) {
            fuel = model.spec.maxFuel
        }
        if (player.uniqueId == owner) {
            placeCarEntity(player, itemStack, event.hand, owner, model, whereToSpawn, fuel)
            return
        }
        if (!Permission.VEHICLE_PLACE.obtained(player)) {
            player.sendActionBar(ChatColor.RED + "乗り物を設置することができませんでした")
            return
        }
        placeCarEntity(player, itemStack, event.hand, owner, model, whereToSpawn, fuel)
    }

    @EventHandler
    fun onPlayerDismount(event: EntityDismountEvent) {
        val dismounted = event.dismounted as CraftEntity
        val entity = event.entity
        if (dismounted.handle !is SeatArmorStand || entity !is Player) {
            return
        }
        // sendActionBar("") doesn't work thanks to paper-api
        entity.sendActionBar(" ")
    }

    private fun placeCarEntity(whoPlaced: Player, carItem: ItemStack, hand: EquipmentSlot?, owner: UUID, model: Model, location: Location, fuel: Float) {
        tow(owner)
        if (spawn(owner, model, location, fuel)) {
            location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f)
            if (hand == EquipmentSlot.OFF_HAND) {
                whoPlaced.inventory.setItemInOffHand(null)
            } else {
                whoPlaced.inventory.remove(carItem)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        StringEditor.close(event.player)
        EditSessions.destroy(event.player.uniqueId)
        val vehicle = event.player.vehicle ?: return
        vehicle.removePassenger(event.player)
    }
}