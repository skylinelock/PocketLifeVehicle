package dev.sky_lock.mocar.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.sky_lock.mocar.car.CarModel;
import org.bukkit.Location;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author sky_lock
 */

public class GsonUtil {

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(CarModel.class, new CarModelAdapter())
            .create();

    /**
     * Fileから指定した型でJsonをロードします。
     * Fileに何も記述がない場合、nullを返します。
     *
     * @param type ロード対象の型
     * @param <T> 型
     * @return Jsonから読み取られた型
     * @throws IOException
     * @throws RuntimeException
     */
    public static <T> T load(Path filePath, Type type) throws IOException {
        checkFile(filePath);
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(filePath, StandardCharsets.UTF_8))) {
            if (!reader.hasNext()) {
                return null;
            }
            return gson.fromJson(reader, type);
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
     */
    public static <T> void save(Path filePath, T obj, Type type) throws IOException {
        checkFile(filePath);
        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(filePath))) {
            gson.toJson(obj, type, writer);
        }
    }

    private static void checkFile(Path filePath) throws IOException {
        if (!Files.isReadable(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
    }
}
