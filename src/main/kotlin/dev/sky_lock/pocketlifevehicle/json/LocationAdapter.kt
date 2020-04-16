package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.*
import org.bukkit.Bukkit
import org.bukkit.Location
import java.lang.reflect.Type

/**
 * @author sky_lock
 */
class LocationAdapter : JsonSerializer<Location>, JsonDeserializer<Location> {
    override fun serialize(location: Location, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        val obj = JsonObject()
        obj.add("world", JsonPrimitive(location.world.name))
        obj.add("x", JsonPrimitive(location.x))
        obj.add("y", JsonPrimitive(location.y))
        obj.add("z", JsonPrimitive(location.z))
        obj.add("yaw", JsonPrimitive(location.yaw))
        obj.add("pitch", JsonPrimitive(location.pitch))
        return obj
    }

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type, jsonDeserializationContext: JsonDeserializationContext): Location {
        val obj = jsonElement.asJsonObject
        val worldName = obj["world"].asString
        val x = obj["x"].asDouble
        val y = obj["y"].asDouble
        val z = obj["z"].asDouble
        val yaw = obj["yaw"].asFloat
        val pitch = obj["pitch"].asFloat
        var world = Bukkit.getWorld(worldName)
        if (world == null) {
            if (Bukkit.getWorlds().isEmpty()) {
                throw JsonParseException("Cannot deserialize bukkit location without any worlds loaded!")
            }
            world = Bukkit.getWorlds()[0]
        }
        return Location(world, x, y, z, yaw, pitch)
    }
}