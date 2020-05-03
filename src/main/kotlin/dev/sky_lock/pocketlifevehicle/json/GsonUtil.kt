package dev.sky_lock.pocketlifevehicle.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Location
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author sky_lock
 */
object GsonUtil {
    private val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Location::class.java, LocationAdapter())
            .create()

    /**
     * Fileから指定した型でJsonをロードします。
     * Fileに何も記述がない場合、nullを返します。
     *
     * @param type ロード対象の型
     * @param <T> 型
     * @return Jsonから読み取られた型
     * @throws IOException
     * @throws RuntimeException
    </T> */
    @Throws(IOException::class)
    fun <T> load(filePath: Path, type: Type): T? {
        checkFile(filePath)
        JsonReader(Files.newBufferedReader(filePath, StandardCharsets.UTF_8)).use { reader ->
            return if (!reader.hasNext()) {
                null
            } else gson.fromJson(reader, type)
        }
    }

    /**
     * Fileに指定した型のオブジェクトをJsonとして保存します。
     *
     * @param obj 保存するインスタンス
     * @param type 保存する型
     * @param <T> 型
     * @throws IOException
     * @throws RuntimeException
    </T> */
    @Throws(IOException::class)
    fun <T> save(filePath: Path, obj: T, type: Type) {
        checkFile(filePath)
        JsonWriter(Files.newBufferedWriter(filePath)).use { writer ->
            writer.setIndent("   ")
            gson.toJson(obj, type, writer)
        }
    }

    @Throws(IOException::class)
    private fun checkFile(filePath: Path) {
        if (!Files.isReadable(filePath)) {
            Files.createDirectories(filePath.parent)
            Files.createFile(filePath)
        }
    }
}