package dev.sky_lock.pocketlifevehicle.vehicle

import net.minecraft.nbt.CompoundTag

/**
 * @author sky_lock
 */

class EntityVehicleHelper {

    companion object {

        fun seatNBT(): CompoundTag {
            val nbt = CompoundTag()
            nbt.putBoolean("NoBasePlate", true)
            nbt.putBoolean("Invulnerable", true)
            nbt.putBoolean("PersistenceRequired", true)
            nbt.putBoolean("NoGravity", false)
            nbt.putBoolean("Invisible", true)
            nbt.putBoolean("Marker", false) // ArmorStand has a very small collision box when true
            return nbt
        }

        fun modelNBT(): CompoundTag {
            val nbt = seatNBT()
            nbt.putBoolean("Small", true)
            return nbt
        }
    }
}