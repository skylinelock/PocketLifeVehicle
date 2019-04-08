package dev.sky_lock.mocar.json;

import com.google.gson.reflect.TypeToken;
import dev.sky_lock.mocar.car.CarEntity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sky_lock
 */

public class CarEntityStoreFile {
    private static final Type TYPETOKEN = new TypeToken<Set<CarEntity>>() {}.getType();
    private final Path filePath;

    public CarEntityStoreFile(Path dirPath) {
        this.filePath = Paths.get(dirPath.toAbsolutePath().toString(), "carentities.json");
    }

    public void save(Set<CarEntity> cars) throws IOException {
        GsonUtil.save(filePath, cars, TYPETOKEN);
    }

    public Set<CarEntity> load() throws IOException {
        Set<CarEntity> carEntities = GsonUtil.load(filePath, TYPETOKEN);
        if (carEntities == null) {
            carEntities = new HashSet<>();
        }
        return Collections.unmodifiableSet(carEntities);
    }

}
