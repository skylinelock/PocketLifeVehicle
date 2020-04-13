package dev.sky_lock.pocketlifevehicle.vehicle.model

import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity.Companion.valueOf
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import java.util.*

/**
 * @author sky_lock
 */
@SerializableAs("Spec")
class Spec(val maxFuel: Float, val maxSpeed: MaxSpeed, val capacity: Capacity, val steeringLevel: SteeringLevel) : ConfigurationSerializable {

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["maxfuel"] = maxFuel
        map["maxspeed"] = maxSpeed.ordinal
        map["capacity"] = capacity.value()
        map["steering"] = steeringLevel.toString()
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Spec {
            val maxFuel = (map["maxfuel"] as Double).toFloat()
            val speed = MaxSpeed.values()[map["maxspeed"] as Int]
            val capacity = valueOf(map["capacity"] as Int)
            val steeringLevel = SteeringLevel.valueOf(map["steering"].toString())
            return Spec(maxFuel, speed, capacity, steeringLevel)
        }
    }

}