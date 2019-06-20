package dev.sky_lock.mocar.config;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sky_lock
 */

class ConfigFiles {
    private static final Logger logger = MoCar.getInstance().getLogger();

    static YamlConfiguration load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Parameter 'path' cannot be null");
        }
        YamlConfiguration config = null;
        createPath(path);

        try (BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            config = YamlConfiguration.loadConfiguration(in);
            config.options().copyDefaults(true);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not load configurations from " + path.toString());
        }
        return config;
    }

    static void save(Path path, YamlConfiguration config) {
        try {
            createPath(path);
            config.save(path.toFile());
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not save configurations to " + path.toString());
        }
    }

    private static void createPath(Path path) {
        Path dir = path.getParent();
        try {
            if (Files.isDirectory(dir) && Files.notExists(dir)) {
                Files.createDirectories(dir);
            }
            if (Files.notExists(path) && !Files.isReadable(path)) {
                Files.createFile(path);
            }
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not create directories or a file");
        }
    }

}
