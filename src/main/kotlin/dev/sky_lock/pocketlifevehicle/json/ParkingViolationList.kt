package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.reflect.TypeToken
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import org.bukkit.entity.Player
import java.io.IOException

/**
 * @author sky_lock
 */

class ParkingViolationList {
    private val plugin = VehiclePlugin.instance
    private val filePath = plugin.dataFolder.toPath().resolve("parkings.json")
    private val typeToken = object : TypeToken<List<ParkingViolation>>() {}.type
    private lateinit var parkingList: MutableList<ParkingViolation>

    fun load() {
        parkingList = try {
            GsonUtil.load(filePath, typeToken) ?: ArrayList()
        } catch (ex: IOException) {
            plugin.logger.warning("駐車違反リストの読み込みに失敗しました")
            ArrayList()
        }
    }

    fun save() {
        try {
            GsonUtil.save(filePath, parkingList, typeToken)
        } catch (ex: IOException) {
            plugin.logger.warning("駐車違反リストを保存しました")
        }
    }

    // 重複は考慮しない
    fun registerNewEntry(entry: ParkingViolation) {
        parkingList.add(entry)
    }

    fun findEntry(player: Player): ParkingViolation? {
        return parkingList.find {entry -> entry.ownerUuid == player.uniqueId}
    }

    fun removeEntry(player: Player) {
        val entry = this.findEntry(player)
        if (entry != null) {
            parkingList.remove(entry)
        }
    }

}