package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.reflect.TypeToken
import dev.sky_lock.pocketlifevehicle.json.GsonUtil.load
import dev.sky_lock.pocketlifevehicle.json.GsonUtil.save
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntity
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * @author sky_lock
 */
class EntityStoreFile(dirPath: Path) {
    private val filePath: Path

    @Throws(IOException::class)
    fun save(cars: Set<CarEntity>) {
        save(filePath, cars, TYPETOKEN)
    }

    @Throws(IOException::class)
    fun load(): Set<CarEntity> {
        var carEntities = load<Set<CarEntity>>(filePath, TYPETOKEN)
        if (carEntities == null) {
            carEntities = HashSet()
        }
        return carEntities
    }

    companion object {
        private val TYPETOKEN = object : TypeToken<Set<CarEntity>>() {}.type
    }

    init {
        filePath = Paths.get(dirPath.toAbsolutePath().toString(), "entities.json")
    }
}