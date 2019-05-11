package dev.sky_lock.mocar;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import dev.sky_lock.menu.InventoryMenuListener;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.SeatArmorStand;
import dev.sky_lock.mocar.command.CommandHandler;
import dev.sky_lock.mocar.config.PluginConfig;
import dev.sky_lock.mocar.item.Glowing;
import dev.sky_lock.mocar.json.CarEntityStoreFile;
import dev.sky_lock.mocar.listener.ChunkEventListener;
import dev.sky_lock.mocar.listener.EventListener;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.function.Function;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private final CarEntityStoreFile carStoreFile = new CarEntityStoreFile(getDataFolder().toPath());
    public static final Material CAR_ITEM = Material.IRON_PICKAXE;

    private PluginConfig pluginConfig;
    public static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Car" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        instance = this;

        pluginConfig = new PluginConfig();

        CommandHandler commandHandler = new CommandHandler();
        this.getCommand("mocar").setExecutor(commandHandler);
        CarEntities.spawnAll();

        this.registerPluginEvents();
        this.registerEntities();
        Glowing.register();
    }

    @Override
    public void onDisable() {
        CarEntities.saveAll();
        ModelList.saveConfig();
        pluginConfig.saveToFile();
    }

    private void registerPluginEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EventListener(), this);
        pluginManager.registerEvents(new ChunkEventListener(), this);
        pluginManager.registerEvents(new InventoryMenuListener(this), this);
    }

    public CarEntityStoreFile getCarStoreFile() {
        return carStoreFile;
    }

    public static MoCar getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    private void registerEntities() {
        registerEntity("car_armor_stand", CarArmorStand.class, CarArmorStand::new);
        registerEntity("seat_armor_stand", SeatArmorStand.class, SeatArmorStand::new);
        this.getLogger().info("Car entities were successfully registered!");
    }

    private <T extends Entity> void registerEntity(String id, Class<T> clazz, Function<? super World, T> function) {
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1631)).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.get("minecraft:armor_stand"));
        EntityTypes.a(id, EntityTypes.a.a(clazz, function));
    }

    public NamespacedKey createKey(String key) {
        return new NamespacedKey(this, key);
    }
}
