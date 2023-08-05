package dev.sky_lock.pocketlifevehicle.util

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

/**
 * @author sky_lock
 */

class Region(location1: Location, location2: Location) {
    val world: World
    private val regloc = arrayOfNulls<Location>(2)
    private val corners = arrayOfNulls<Location>(8)
    private val lowX: Int
    private val lowY: Int
    private val lowZ: Int
    private val highX: Int
    private val highY: Int
    private val highZ: Int

    init {
        check(location1.world == location2.world)
        world = location1.world
        regloc[0] = location1
        regloc[1] = location2
        lowX = min(location1.blockX, location2.blockX)
        lowY = min(location1.blockY, location2.blockY)
        lowZ = min(location1.blockZ, location2.blockZ)
        highX = max(location1.blockX, location2.blockX)
        highY = max(location1.blockY, location2.blockY)
        highZ = max(location1.blockZ, location2.blockZ)
        corners[0] = world.getBlockAt(lowX, highY, highZ).location
        corners[1] = world.getBlockAt(highX, highY, highZ).location
        corners[2] = world.getBlockAt(highX, highY, lowZ).location
        corners[3] = world.getBlockAt(lowX, highY, lowZ).location
        corners[4] = world.getBlockAt(lowX, lowY, highZ).location
        corners[5] = world.getBlockAt(highX, lowY, highZ).location
        corners[6] = world.getBlockAt(highX, lowY, lowZ).location
        corners[7] = world.getBlockAt(lowX, lowY, lowZ).location
    }

    operator fun contains(player: Player): Boolean {
        return contains(player.location)
    }

    operator fun contains(location: Location): Boolean {
        val x = overlap1D(lowX.toDouble(), highX.toDouble(), location.blockX.toDouble(), location.blockX.toDouble())
        val y = overlap1D(lowY.toDouble(), highY.toDouble(), location.blockY.toDouble(), location.blockY.toDouble())
        val z = overlap1D(lowZ.toDouble(), highZ.toDouble(), location.blockZ.toDouble(), location.blockZ.toDouble())
        return x && y && z
    }

    private fun overlap1D(low1: Double, high1: Double, low2: Double, high2: Double): Boolean {
        return if (low1 <= low2) {
            low2 <= high1
        } else low1 <= high2
    }
}
