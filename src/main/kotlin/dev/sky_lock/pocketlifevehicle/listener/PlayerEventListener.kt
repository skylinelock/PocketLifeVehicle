package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.removeWhiteSpace
import dev.sky_lock.pocketlifevehicle.gui.EditSessions
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import dev.sky_lock.pocketlifevehicle.json.ParkingViolation
import dev.sky_lock.pocketlifevehicle.vehicle.ModelArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.SeatArmorStand
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntities
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
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

class PlayerEventListener: Listener {

    val plugin = VehiclePlugin.instance

    @EventHandler
    fun onPlayerDismount(event: EntityDismountEvent) {
        val dismounted = event.dismounted as CraftEntity
        val entity = event.entity
        if (dismounted.handle !is SeatArmorStand || entity !is Player) {
            return
        }
        entity.sendActionBar(" ")
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // TODO: ここほんとゴミ
        val uuid = event.player.uniqueId
        val vehicle = VehicleEntities.of(uuid)
        if (vehicle != null) {
            val entry = ParkingViolation(Date(), uuid, vehicle.model.id, vehicle.status.fuel)
            this.plugin.parkingViolationList.registerNewEntry(entry)
            vehicle.remove()
        }

        StringEditor.close(event.player)
        EditSessions.destroy(event.player.uniqueId)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }
        val itemStack = event.item ?: return
        if (!itemStack.hasItemMeta()) {
            return;
        }
        val meta = itemStack.itemMeta
        val player = event.player
        val model = Storage.MODEL.findByItemStack(itemStack) ?: return
        event.isCancelled = true
        event.setUseInteractedBlock(Event.Result.DENY)
        event.setUseItemInHand(Event.Result.DENY)
        if (!this.plugin.pluginConfiguration.isWorldVehicleCanPlaced(player.world)) {
            player.sendActionBar(ChatColor.RED + "このワールドでは乗り物は使用できません")
            return
        }
        if (this.plugin.parkingViolationList.findEntry(player) != null) {
            player.sendActionBar(ChatColor.RED + "乗り物に乗るには駐車違反料を支払う必要があります")
            return
        }
        if (event.blockFace != BlockFace.UP) {
            player.sendActionBar(ChatColor.RED + "乗り物は地面にのみ設置できます")
            return
        }
        val block = event.clickedBlock ?: return
        val whereToSpawn = block.location.clone().add(0.5, 1.0, 0.5)
        val hand = event.hand ?: return // This should be null when event.action == Action.PHYSICAL
        val ownerUid = meta.persistentDataContainer.get(VehiclePlugin.instance.createKey("owner"), PersistentDataType.STRING)
        if (ownerUid == null) {
            placeVehicleEntity(player, itemStack, hand, player.uniqueId, model, player.location, model.spec.maxFuel)
            return
        }
        val owner = UUID.fromString(ownerUid)
        val lore = meta.lore
        if (lore == null) {
            placeVehicleEntity(player, itemStack, hand, player.uniqueId, model, player.location, model.spec.maxFuel)
            return
        }
        val rawFuel = lore[1]
        var fuel = rawFuel.removeWhiteSpace().split(":".toRegex()).toTypedArray()[1].toFloat()
        if (fuel > model.spec.maxFuel) {
            fuel = model.spec.maxFuel
        }
        if (player.uniqueId == owner) {
            placeVehicleEntity(player, itemStack, hand, owner, model, whereToSpawn, fuel)
            return
        }
        if (!Permission.VEHICLE_PLACE.obtained(player)) {
            player.sendActionBar(ChatColor.RED + "乗り物を設置することができませんでした")
            return
        }
        placeVehicleEntity(player, itemStack, hand, owner, model, whereToSpawn, fuel)
    }

    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) {
            return
        }
        val armorStand = event.rightClicked as CraftArmorStand
        val player = event.player

        val handle = armorStand.handle
        val clicked = player.uniqueId
        val vehicle = VehicleEntities.getVehicle(armorStand) ?: return
        val owner = VehicleEntities.getOwner(vehicle) ?: return

        val ownerName = VehicleEntities.getOwnerName(vehicle)

        event.isCancelled = true

        if (player.isSneaking) {
            if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                sendRefusedReason(player, "この乗り物は $ownerName が所有しています")
                return
            }
            vehicle.openMenu(player)
            return
        }

        if (handle is SeatArmorStand) {
            if (armorStand.passengers.isNotEmpty()) {
                return
            }
            if (clicked == owner) {
                armorStand.addPassenger(player)
                return
            }
            if (vehicle.status.isLocked) {
                sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                return
            }
            armorStand.addPassenger(player)
        } else if (handle is ModelArmorStand) {
            if (vehicle.passengers.size >= vehicle.model.spec.capacity.value()) {
                sendRefusedReason(player, "この乗り物は満員です")
                return
            }
            if (clicked == owner) {
                vehicle.addPassenger(player)
                return
            }
            if (vehicle.status.isLocked) {
                sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                return
            }
            vehicle.addPassenger(player)
        }
    }

    private fun placeVehicleEntity(whoPlaced: Player, carItem: ItemStack, hand: EquipmentSlot, owner: UUID, model: Model, location: Location, fuel: Float) {
        VehicleEntities.tow(owner)
        if (VehicleEntities.spawn(owner, model, location, fuel)) {
            location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f)
            if (hand == EquipmentSlot.OFF_HAND) {
                whoPlaced.inventory.setItemInOffHand(null)
            } else {
                whoPlaced.inventory.remove(carItem)
            }
        }
    }

    private fun sendRefusedReason(player: Player, message: String) {
        player.sendActionBar(ChatColor.YELLOW + ChatColor.BOLD + "⚠⚠ " + ChatColor.RED + "" + ChatColor.BOLD + message + ChatColor.YELLOW + "" + ChatColor.BOLD + " ⚠⚠")
    }
}