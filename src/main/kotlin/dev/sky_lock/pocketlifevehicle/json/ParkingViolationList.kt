package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.reflect.TypeToken
import dev.sky_lock.pocketlifevehicle.VehiclePlugin
import java.io.IOException
import java.util.*

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

    fun findEntry(owner: UUID): ParkingViolation? {
        return parkingList.find {entry -> entry.ownerUuid == owner}
    }

    fun removeEntry(owner: UUID): Boolean {
        val entry = this.findEntry(owner)
        return if (entry == null) false else {
            parkingList.remove(entry)
            true
        }
    }

}