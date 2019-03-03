package dev.sky_lock.mocar.config;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

/**
 * @author sky_lock
 */

class ConfigurationFiles {

    static YamlConfiguration load(String name) {
        YamlConfiguration config = null;
        saveDefault(Paths.get(name));
        Path path = MoCar.getInstance().getDataFolder().toPath().resolve(name);

        try (BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8);) {
            config = YamlConfiguration.loadConfiguration(in);
            config.options().copyDefaults(true);
        } catch (IOException ex) {
            MoCar.getInstance().getLogger().log(Level.WARNING, "Could not load " + path.toString());
            return null;
        }
        return config;
    }

    static void save(Path path, YamlConfiguration config) {
        saveDefault(path);
        try {
            config.save(path.toFile());
        } catch (IOException ex) {
            ex.printStackTrace();
            MoCar.getInstance().getLogger().log(Level.WARNING, "Could not save " + path.toString());
        }
    }

    private static void saveDefault(Path path) {
        if (!Files.isReadable(path)) {
            MoCar.getInstance().saveResource(path.getFileName().toString(), false);
        }
    }
}
