package dev.sky_lock.pocketlifevehicle.vehicle.model

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs

/**
 * @author sky_lock
 */
@SerializableAs("Spec")
class Spec(var maxFuel: Float, var maxSpeed: MaxSpeed, var steeringLevel: SteeringLevel) : ConfigurationSerializable {

    override fun serialize(): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["maxfuel"] = maxFuel
        map["maxspeed"] = maxSpeed.ordinal
        map["steering"] = steeringLevel.ordinal
        return map
    }

    companion object {
        @JvmStatic
        fun deserialize(map: Map<String, Any>): Spec {
            val maxFuel = (map["maxfuel"] as Double).toFloat()
            val speed = MaxSpeed.values()[map["maxspeed"] as Int]
            val steeringLevel = SteeringLevel.values()[map["steering"] as Int]
            return Spec(maxFuel, speed, steeringLevel)
        }
    }

}