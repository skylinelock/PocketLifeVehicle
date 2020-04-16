package dev.sky_lock.pocketlifevehicle

import dev.sky_lock.menu.InventoryMenuListener
import dev.sky_lock.pocketlifevehicle.command.CommandHandler
import dev.sky_lock.pocketlifevehicle.config.PluginConfiguration
import dev.sky_lock.pocketlifevehicle.item.GlowEnchantment
import dev.sky_lock.pocketlifevehicle.json.EntityStoreFile
import dev.sky_lock.pocketlifevehicle.listener.ChunkEventListener
import dev.sky_lock.pocketlifevehicle.listener.EventListener
import dev.sky_lock.pocketlifevehicle.vehicle.CarEntities
import dev.sky_lock.pocketlifevehicle.vehicle.Storage
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author sky_lock
 */

class PLVehicle : JavaPlugin() {

    companion object {
        lateinit var instance: PLVehicle
        val PREFIX = ChatColor.DARK_GRAY.toString() + "[" + ChatColor.DARK_GREEN.toString() + "PLVehicle" + ChatColor.DARK_GRAY.toString() + "] " + ChatColor.RESET.toString()
    }

    lateinit var pluginConfiguration: PluginConfiguration
    val entityStoreFile: EntityStoreFile = EntityStoreFile(dataFolder.toPath())

    override fun onEnable() {
        super.onEnable()
        instance = this
        pluginConfiguration = PluginConfiguration()
        val commandHandler = CommandHandler()
        getCommand("vehicle")?.setExecutor(commandHandler)
        CarEntities.spawnAll()

        registerPluginEvents()
        registerEntities()

        try {
            val field = Enchantment::class.java.getDeclaredField("acceptingNew")
            field.isAccessible = true
            field.set(null, true)
        } catch (ex: NoSuchFileException) {
            logger.warning("Could not register the enchant for growing item")
        } catch (ex: IllegalAccessException) {
            logger.warning("Could not register the enchant for growing item")
        }
        Enchantment.registerEnchantment(GlowEnchantment())
    }

    override fun onDisable() {
        super.onDisable()
        CarEntities.saveAll()
        Storage.MODEL.saveToFile()
        pluginConfiguration.save()
    }

    private fun registerPluginEvents() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(EventListener(), this)
        pluginManager.registerEvents(ChunkEventListener(), this)
        pluginManager.registerEvents(InventoryMenuListener(this), this)
    }

    private fun registerEntities() {
/*        registerEntity("car_armor_stand", (type, world) -> new CarArmorStand(type, world));
        registerEntity("seat_armor_stand", SeatArmorStand::new);*/
        logger.info("Car entities were successfully registered!")
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

    fun createKey(key: String): NamespacedKey {
        return NamespacedKey(this, key)
    }

}