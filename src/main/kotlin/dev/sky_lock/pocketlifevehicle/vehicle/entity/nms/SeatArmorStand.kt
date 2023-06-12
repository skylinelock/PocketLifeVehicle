package dev.sky_lock.pocketlifevehicle.vehicle.entity.nms

import dev.sky_lock.pocketlifevehicle.Keys
import dev.sky_lock.pocketlifevehicle.text.ext.sendActionBar
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleManager
import dev.sky_lock.pocketlifevehicle.vehicle.entity.EntityVehicle
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.level.Level
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sqrt

/**
 *
 *
 *
 *
 * @author sky_lock
 */
class SeatArmorStand(entityTypes: EntityType<ArmorStand>, world: Level) :
    BaseArmorStand<SeatArmorStand>(entityTypes, world) {
    lateinit var vehicleId: UUID
    lateinit var entityVehicle: EntityVehicle

    override fun saveWithoutId(nbt: CompoundTag): CompoundTag {
        val parent = super.saveWithoutId(nbt)
        parent.putUUID(Keys.ID.label, vehicleId)
        return parent
    }

    override fun load(nbt: CompoundTag) {
        super.load(nbt)
        if (!nbt.hasUUID(Keys.ID.label)) return
        this.vehicleId = nbt.getUUID(Keys.ID.label)
        val v = VehicleManager.findOrNull(vehicleId)
        if (v == null) {
            discard()
            return
        }
        this.entityVehicle = v
    }

    // 毎tick呼び出される
    override fun aiStep() {
        // setDependingValue()
        if (!::entityVehicle.isInitialized) {
            super.aiStep()
            return
        }
        val position = VehicleManager.getSeatId(bukkitEntity as org.bukkit.entity.ArmorStand)
        if (passengers.isEmpty()) {
            updatePosition(position)
            return
        }
        val passenger = passengers[0] as LivingEntity
        if (passenger !is net.minecraft.world.entity.player.Player) {
            updatePosition(position)
            return
        }
        if (position == 0) {
            (passenger.bukkitEntity as Player).sendActionBar(entityVehicle.createMeterPanelLine())
        }
        updatePosition(position)
    }

    private fun updatePosition(position: Int) {
        val loc = entityVehicle.location.clone()
        val seatOption = entityVehicle.model.seatOption
        val capacity = entityVehicle.seats.size

        val offset = seatOption.offset
        val depth = seatOption.depth
        val width = seatOption.width

        val unit = loc.direction
        val vec = unit.clone().multiply(offset)
        val origin = loc.add(vec)

        if (capacity == 1) {
            setPosRot(origin)
            return
        }
        if (capacity == 2) {
            if (position != 0) {
                val vec2 = unit.clone().rotateAroundY(Math.PI).multiply(depth)
                origin.add(vec2)
            }
            setPosRot(origin)
            return
        }

        val distance = sqrt((depth.pow(2) + width.pow(2)).toDouble())
        val rad = atan(2 * depth / width)
        val vertical = Math.PI / 2
        val theta = if (rad.isNaN()) vertical else rad.toDouble()

        if (capacity != 4) {
            throw java.lang.IllegalStateException("Unknown capacity?")
        }
        var vec2 = unit
        if (position == 0) {
            vec2 = vec2.multiply(width / 2).rotateAroundY(-vertical)
        }
        if (position == 1) {
            vec2 = vec2.multiply(width / 2).rotateAroundY(vertical)
        }
        if (position == 2) {
            vec2 = vec2.multiply(distance).rotateAroundY(vertical + theta)
        }
        if (position == 3) {
            vec2 = vec2.multiply(distance).rotateAroundY(-(vertical + theta))
        }
        setPosRot(origin.add(vec2))
    }

    private fun setPosRot(loc: Location) {
        xo = loc.x
        yo = entityVehicle.location.y - 1.675 + entityVehicle.model.height
        zo = loc.z
        setPos(xo, yo, zo)
        yRot = entityVehicle.location.yaw
        yRotO = yRot
        xRot = entityVehicle.location.pitch
        setRot(yRot, xRot)
    }

}