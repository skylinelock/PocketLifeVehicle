package dev.sky_lock.pocketlifevehicle.vehicle

import org.bukkit.Location
import java.util.*

/**
 * @author sky_lock
 */
class VehicleEntity internal constructor(private val ownerUuid: String, val modelId: String, val location: Location, val fuel: Float) {
    val owner: UUID
        get() = UUID.fromString(ownerUuid)

}