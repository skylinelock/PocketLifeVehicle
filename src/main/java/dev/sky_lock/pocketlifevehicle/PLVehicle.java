package dev.sky_lock.pocketlifevehicle;

import dev.sky_lock.menu.InventoryMenuListener;
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import dev.sky_lock.pocketlifevehicle.command.CommandHandler;
import dev.sky_lock.pocketlifevehicle.config.PluginConfig;
import dev.sky_lock.pocketlifevehicle.item.Glowing;
import dev.sky_lock.pocketlifevehicle.json.CarEntityStoreFile;
import dev.sky_lock.pocketlifevehicle.listener.ChunkEventListener;
import dev.sky_lock.pocketlifevehicle.listener.EventListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author sky_lock
 */

public class PLVehicle extends JavaPlugin {

    private static PLVehicle instance;
    private final CarEntityStoreFile carStoreFile = new CarEntityStoreFile(getDataFolder().toPath());
    public static final Material CAR_ITEM = Material.IRON_NUGGET;

    private PluginConfig pluginConfig;
    public static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "PLVehicle" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        instance = this;

        pluginConfig = new PluginConfig();

        CommandHandler commandHandler = new CommandHandler();
        this.getCommand("vehicle").setExecutor(commandHandler);
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

    public static PLVehicle getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

    private void registerEntities() {
/*        registerEntity("car_armor_stand", (type, world) -> new CarArmorStand(type, world));
        registerEntity("seat_armor_stand", SeatArmorStand::new);*/
        this.getLogger().info("Car entities were successfully registered!");
    }

/*    private <T extends Entity> void registerEntity(String id, EntityTypes.b<T> constructor) {
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1631)).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.get("minecraft:armor_stand"));
        try {
            Method method = EntityTypes.class.getMethod("a", EntityTypes.b.class, EnumCreatureType.class);
            method.setAccessible(true);
            method.invoke(id, EntityTypes.a.a(constructor, EnumCreatureType.MISC).a(0.5F, 1.975F));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            this.getLogger().warning("Could not register custom entities");
        }
    }*/

    public NamespacedKey createKey(String key) {
        return new NamespacedKey(this, key);
    }
}
