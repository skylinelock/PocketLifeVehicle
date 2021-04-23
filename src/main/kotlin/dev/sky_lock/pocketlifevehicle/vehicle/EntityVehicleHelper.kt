package dev.sky_lock.pocketlifevehicle.vehicle

import net.minecraft.server.v1_14_R1.NBTTagCompound

/**
 * @author sky_lock
 */

class EntityVehicleHelper {

    companion object {

        fun seatNBT(): NBTTagCompound {
            val nbt = NBTTagCompound()
            nbt.setBoolean("NoBasePlate", true)
            nbt.setBoolean("Invulnerable", true)
            nbt.setBoolean("PersistenceRequired", true)
            nbt.setBoolean("NoGravity", false)
            nbt.setBoolean("Invisible", true)
            nbt.setBoolean("Marker", false) // ArmorStand has a very small collision box when true
            return nbt
        }

        fun modelNBT(): NBTTagCompound {
            val nbt = seatNBT()
            nbt.setBoolean("Small", true)
            return nbt
        }
    }
}