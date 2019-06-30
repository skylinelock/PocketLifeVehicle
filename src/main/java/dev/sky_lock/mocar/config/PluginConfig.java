package dev.sky_lock.mocar.config;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class PluginConfig {

    private static final MoCar plugin = MoCar.getInstance();
    private final FileConfiguration config;

    public PluginConfig() {
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
        config.options().copyDefaults(true);
    }

    public Set<World> getAllowWorlds() {
        return config.getStringList("allow-worlds").stream()
                .filter(
                        name -> Bukkit.getWorlds().stream().anyMatch(world -> world.getName().equalsIgnoreCase(name))
                )
                .map(Bukkit::getWorld).collect(Collectors.toSet());
    }

    public int getWarningCount() {
        int count = config.getInt("warning-count");
        if (count < 1) {
            count = 5;
        }
        return count;
    }

    public void setWarningCount(int count) {
        config.set("warning-count", count);
    }

    public void addAllowWorld(String name) {
        List<String> allowWorlds = config.getStringList("allow-worlds");
        allowWorlds.add(name);
        config.set("allow-worlds", allowWorlds);
    }

    public void removeAllWorld(String name) {
        List<String> allowWorlds = config.getStringList("allow-worlds");
        allowWorlds.remove(name);
        config.set("allow-worlds", allowWorlds);
    }

    public void saveToFile() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save configurations");
        }
    }

    public void reloadFromDisk() {
        try {
            config.load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException | InvalidConfigurationException ex) {
            plugin.getLogger().warning("Failed to load config.yml");
        }
    }

}
