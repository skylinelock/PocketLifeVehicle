package dev.sky_lock.mocar;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.sky_lock.glassy.gui.InventoryMenuListener;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.command.CommandHandler;
import dev.sky_lock.mocar.config.PluginConfig;
import dev.sky_lock.mocar.item.Glowing;
import dev.sky_lock.mocar.json.CarEntityStoreFile;
import dev.sky_lock.mocar.listener.GuiListener;
import dev.sky_lock.mocar.listener.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private PluginConfig pluginConfig;
    private final CarEntityStoreFile carEntityStoreFile = new CarEntityStoreFile(getDataFolder().toPath());
    private ProtocolManager protocolManager;
    public static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Car" + ChatColor.DARK_GRAY +"] " + ChatColor.RESET;


    @Override
    public void onEnable() {
        instance = this;
        pluginConfig = new PluginConfig();
        protocolManager = ProtocolLibrary.getProtocolManager();

        CommandHandler commandHandler = new CommandHandler();
        getCommand("mocar").setExecutor(commandHandler);

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryMenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        Glowing.register();
        spawnCarEntities();
    }

    @Override
    public void onDisable() {
        ModelList.saveConfig();
        saveCarEntities();
        pluginConfig.saveToFile();
    }

    public static MoCar getInstance() {
        return instance;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    private void spawnCarEntities() {
        try {
            carEntityStoreFile.load().forEach(carEntity -> {
                CarEntities.spawn(carEntity.getOwner(), carEntity.getModel(), carEntity.getLocation(), carEntity.getFuel());
            });
        } catch (IOException ex) {
            getLogger().warning("CarEntityの読み込みに失敗しました");
        }
    }

    private void saveCarEntities() {
        try {
            carEntityStoreFile.save(CarEntities.getCarEntities());
            CarEntities.killAll();
        } catch (IOException ex) {
            getLogger().warning("CarEntityの保存に失敗しました");
        }
    }
}
