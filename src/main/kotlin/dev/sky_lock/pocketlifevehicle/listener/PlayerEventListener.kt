package dev.sky_lock.pocketlifevehicle.listener

import dev.sky_lock.pocketlifevehicle.Permission
import dev.sky_lock.pocketlifevehicle.PluginKey
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import dev.sky_lock.pocketlifevehicle.item.UUIDTagType
import dev.sky_lock.pocketlifevehicle.text.Line
import dev.sky_lock.pocketlifevehicle.text.ext.sendActionBar
import dev.sky_lock.pocketlifevehicle.text.ext.sendMessage
import dev.sky_lock.pocketlifevehicle.vehicle.ModelRegistry
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicleFacade
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.spigotmc.event.entity.EntityDismountEvent
import java.util.*

/**
 * @author sky_lock
 */

class PlayerEventListener : Listener {

    private val plugin = VehiclePlugin.instance

    @EventHandler
    fun onPlayerDismount(event: EntityDismountEvent) {
        val entity = event.entity
        val vehicle = EntityVehicleFacade.fromBukkit(event.dismounted) ?: return
        if (vehicle.isSeat() && entity is Player) {
            entity.sendActionBar(Component.empty())
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (plugin.parkingViolationList.findEntry(player.uniqueId) != null) {
            player.sendMessage(Line().redBold("駐車違反登録されています。乗り物を利用するにはスマホから駐車違反料を払う必要があります。"))
        }
    }

    // プレイヤーが自主的にログアウトした時のみ呼ばれる
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        VehicleManager.registerIllegalParking(player.uniqueId)
        val riding = player.vehicle ?: return
        EntityVehicleFacade.fromBukkit(riding)?.dismount(player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerInteractOnVehicle(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK &&
            event.action != Action.RIGHT_CLICK_AIR) return
        val mount = event.player.vehicle ?: return
        val vehicle = EntityVehicleFacade.fromBukkit(mount) ?: return
        if (!vehicle.isSeat()) return
        if (!vehicle.isDriverSeat()) return
        val player = event.player
        val loc = player.location
        loc.yaw = vehicle.getYaw()
        player.teleport(loc)
        mount.addPassenger(player)
    }

    // メインハンド、オフハンドごとに２回呼ばれる
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val player = event.player

        val itemInMainHand = player.inventory.itemInMainHand
        val itemInOffHand = player.inventory.itemInOffHand
        if (!itemInMainHand.hasItemMeta() && !itemInOffHand.hasItemMeta()) {
            return
        }
        var model: Model? = null
        var item: ItemStack? = null
        val mainModel = ModelRegistry.findByItemStack(itemInMainHand)
        val offModel = ModelRegistry.findByItemStack(itemInOffHand)
        if (event.hand == EquipmentSlot.HAND) {
            if (mainModel != null && offModel == null) {
                item = itemInMainHand
                model = mainModel
            }
        } else if (event.hand == EquipmentSlot.OFF_HAND) {
            if (offModel != null) {
                if (mainModel == null) {
                    model = offModel
                    item = itemInOffHand
                } else {
                    model = mainModel
                    item = itemInMainHand
                }
            }
        } else {
            return
        }
        if (model == null || item == null) return

        event.isCancelled = true
        event.setUseInteractedBlock(Event.Result.DENY)
        event.setUseItemInHand(Event.Result.DENY)

        if (!this.plugin.pluginConfiguration.isWorldVehicleCanPlaced(player.world)) {
            player.sendActionBar(Line().red("このワールドでは乗り物は使用できません"))
            return
        }
        if (this.plugin.parkingViolationList.findEntry(player.uniqueId) != null) {
            player.sendActionBar(Line().red("乗り物を利用するにはスマホから駐車違反料を支払う必要があります"))
            return
        }
        // if (VehicleManager.hasVehicle())
        if (event.blockFace != BlockFace.UP) {
            player.sendActionBar(Line().red("乗り物は地面にのみ設置できます"))
            return
        }
        val block = event.clickedBlock ?: return
        val where = block.location.clone().add(0.5, 1.0, 0.5)
        if (!VehicleManager.verifyPlaceableLocation(where)) {
            player.sendActionBar(Line().red("ブロックがあるので乗り物を設置できません"))
            return
        }
        where.yaw = player.eyeLocation.yaw

        val container = item.itemMeta.persistentDataContainer
        val owner = container.get(PluginKey.OWNER, UUIDTagType.INSTANCE)
        var fuel = container.get(PluginKey.FUEL, PersistentDataType.FLOAT)
        if (owner == null || fuel == null) {
            placeVehicleEntity(item, player.uniqueId, model, where, model.spec.maxFuel)
            return
        }
        if (fuel > model.spec.maxFuel) {
            fuel = model.spec.maxFuel
        }
        if (player.uniqueId == owner) {
            placeVehicleEntity(item, owner, model, where, fuel)
            return
        }
        if (!Permission.PLACE_OTHER_VEHICLE.obtained(player)) {
            player.sendActionBar(Line().red("この乗り物を所有していません"))
            return
        }
        placeVehicleEntity(item, owner, model, where, fuel)
    }

    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (event.rightClicked.type != EntityType.ARMOR_STAND) {
            return
        }
        val armorStand = event.rightClicked as CraftArmorStand
        val player = event.player

        val vehicle = EntityVehicleFacade.fromBukkit(armorStand) ?: return

        event.isCancelled = true
        val owner = vehicle.getOwner()
        if (owner == null) {
            if (Permission.OPEN_EVENT_VEHICLE_GUI.obtained(player) && player.isSneaking) {
                vehicle.showEventVehicleUtility(player)
                return
            }
            if (vehicle.isSeat()) {
                if (vehicle.isOccupied()) return
                if (vehicle.isLocked()) {
                    sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                    return
                }
                armorStand.addPassenger(player)
                return
            }
            if (!vehicle.isModel()) {
                return
            }
            if (vehicle.hasReachedCapacity()) {
                sendRefusedReason(player, "この乗り物は満員です")
                return
            }
            if (vehicle.isLocked()) {
                sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                return
            }
            vehicle.mount(player)
            return
        }

        val ownerName = vehicle.getOwnerName()
        val clicked = player.uniqueId

        if (player.isSneaking) {
            if (clicked != owner && !Permission.OPEN_VEHICLE_GUI.obtained(player)) {
                sendRefusedReason(player, "この乗り物は $ownerName が所有しています")
                return
            }
            vehicle.showVehicleUtility(player)
            return
        }

        if (vehicle.isSeat()) {
            if (armorStand.passengers.isNotEmpty()) return
            if (clicked == owner) {
                armorStand.addPassenger(player)
                return
            }
            if (vehicle.isLocked()) {
                sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                return
            }
            armorStand.addPassenger(player)
        } else if (vehicle.isModel()) {
            if (vehicle.hasReachedCapacity()) {
                sendRefusedReason(player, "この乗り物は満員です")
                return
            }
            if (clicked == owner) {
                vehicle.mount(player)
                return
            }
            if (vehicle.isLocked()) {
                sendRefusedReason(player, "この乗り物には鍵が掛かっています")
                return
            }
            vehicle.mount(player)
        }
    }

    private fun placeVehicleEntity(
        vehicleStack: ItemStack,
        owner: UUID,
        model: Model,
        location: Location,
        fuel: Float
    ) {
        // VehicleManager.pop(owner)
        VehicleManager.placeVehicle(owner, location, model, fuel)
        location.world.playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f)
        vehicleStack.subtract()
    }

    private fun sendRefusedReason(player: Player, message: String) {
        player.sendActionBar(Line().yellowBold("⚠⚠ ").redBold(message).yellowBold(" ⚠⚠"))
    }
}