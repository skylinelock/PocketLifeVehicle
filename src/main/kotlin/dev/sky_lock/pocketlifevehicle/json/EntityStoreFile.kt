package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.reflect.TypeToken
import dev.sky_lock.pocketlifevehicle.json.GsonUtil.load
import dev.sky_lock.pocketlifevehicle.json.GsonUtil.save
import dev.sky_lock.pocketlifevehicle.vehicle.VehicleEntity
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * @author sky_lock
 */
class EntityStoreFile(dirPath: Path) {
    private val filePath = Paths.get(dirPath.toAbsolutePath().toString(), "entities.json")

    @Throws(IOException::class)
    fun save(vehicles: Set<VehicleEntity>) {
        save(filePath, vehicles, TYPETOKEN)
    }

    @Throws(IOException::class)
    fun load(): Set<VehicleEntity> {
        var entities = load<Set<VehicleEntity>>(filePath, TYPETOKEN)
        if (entities == null) {
            entities = HashSet()
        }
        return entities
    }

    companion object {
        private val TYPETOKEN = object : TypeToken<Set<VehicleEntity>>() {}.type
    }

}