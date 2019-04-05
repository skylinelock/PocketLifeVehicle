package dev.sky_lock.mocar;

import com.mojang.datafixers.types.Type;
import dev.sky_lock.glassy.gui.InventoryMenuListener;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.SeatArmorStand;
import dev.sky_lock.mocar.command.CommandHandler;
import dev.sky_lock.mocar.config.PluginConfig;
import dev.sky_lock.mocar.item.Glowing;
import dev.sky_lock.mocar.json.CarEntityStoreFile;
import dev.sky_lock.mocar.listener.EventListener;
import net.minecraft.server.v1_13_R2.DataConverterRegistry;
import net.minecraft.server.v1_13_R2.DataConverterTypes;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.function.Function;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private final CarEntityStoreFile carStoreFile = new CarEntityStoreFile(getDataFolder().toPath());

    private PluginConfig pluginConfig;
    public static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "Car" + ChatColor.DARK_GRAY +"] " + ChatColor.RESET;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        instance = this;

        register("car_armor_stand", CarArmorStand.class, CarArmorStand::new);
        register("seat_armor_stand", SeatArmorStand.class, SeatArmorStand::new);
        pluginConfig = new PluginConfig();

        CommandHandler commandHandler = new CommandHandler();
        getCommand("mocar").setExecutor(commandHandler);
        CarEntities.spawnAll();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryMenuListener(this), this);

        Glowing.register();
    }

    @Override
    public void onDisable() {
        CarEntities.saveAll();
        ModelList.saveConfig();
        pluginConfig.saveToFile();
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

    private <T extends net.minecraft.server.v1_13_R2.Entity> void register(String id, Class<? extends T> clazz, Function<World, ? extends T> function) {
        /*Object[] typeByID = null;
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(1631)).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.of("minecraft:armor_stand"));

        typeByID = Reflection.getFieldValue(Reflection.getField(RegistryID.class, "d"), Reflection.getFieldValue(Reflection.getField(RegistryMaterials.class, "b"), IRegistry.ENTITY_TYPE));

        Object plainArmorStand = typeByID[1];
        EntityTypes<T> ENTITY_TYPE = EntityTypes.a.a(clazz, function).a(id);
        IRegistry.ENTITY_TYPE.a(1, new MinecraftKey(id), ENTITY_TYPE);
        typeByID[1] = plainArmorStand;*/
        Map<Object, Type<?>> types = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        types.put("minecraft:" + id, types.get("minecraft:armor_stand"));

        Bukkit.getLogger().info(EntityTypes.a(id, EntityTypes.a.a(clazz, function)).d());
    }
}
