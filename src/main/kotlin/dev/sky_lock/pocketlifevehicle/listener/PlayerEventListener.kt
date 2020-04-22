package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.PLVehicle
import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.extension.chat.plus
import dev.sky_lock.pocketlifevehicle.extension.kotlin.removeWhiteSpace
import dev.sky_lock.pocketlifevehicle.gui.EditSessions
import dev.sky_lock.pocketlifevehicle.gui.StringEditor
import dev.sky_lock.pocketlifevehicle.util.Profiles
import dev.sky_lock.pocketlifevehicle.vehicle.*
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

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        StringEditor.close(event.player)
        EditSessions.destroy(event.player.uniqueId)
        val vehicle = event.player.vehicle ?: return
        vehicle.removePassenger(event.player)
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
    fun onPlayerInteract(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) {
            return
        }
        val armorStand = event.rightClicked as CraftArmorStand
        if (armorStand.handle !is ModelArmorStand && armorStand.handle !is SeatArmorStand) {
            return
        }
        event.isCancelled = true
        val player = event.player

        val handle = armorStand.handle
        val clicked = player.uniqueId
        val vehicle: Vehicle
        if (handle is SeatArmorStand) {
            if (armorStand.passengers.isNotEmpty()) {
                return
            }
            vehicle = VehicleEntities.getCar(handle) ?: return
            val owner = VehicleEntities.getOwner(vehicle) ?: return
            val ownerName = Profiles.getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendRefusedReason(player, "この乗り物は $ownerName が所有しています")
                    return
                }
                vehicle.openMenu(player)
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
            vehicle = VehicleEntities.getCar(handle) ?: return
            val owner = VehicleEntities.getOwner(vehicle) ?: return
            val ownerName = Profiles.getName(owner)

            if (player.isSneaking) {
                if (clicked != owner && !Permission.VEHICLE_OPEN_GUI.obtained(player)) {
                    sendRefusedReason(player, "この乗り物は $ownerName が所有しています")
                    return
                }
                vehicle.openMenu(player)
                return
            }
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

    private fun placeCarEntity(whoPlaced: Player, carItem: ItemStack, hand: EquipmentSlot?, owner: UUID, model: Model, location: Location, fuel: Float) {
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