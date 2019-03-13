package dev.sky_lock.mocar;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.sky_lock.mocar.car.CarSet;
import dev.sky_lock.mocar.commands.CommandHandler;
import dev.sky_lock.mocar.listener.GuiListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author sky_lock
 */

public class MoCar extends JavaPlugin {

    private static MoCar instance;
    private ProtocolManager protocolManager;
    private CarSet handler;
    public static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "MoCar" + ChatColor.DARK_GRAY +"] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        getCommand("mocar").setExecutor(new CommandHandler());
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        handler = new CarSet();
    }

    @Override
    public void onDisable() {
        handler.saveConfig();
    }

    public static MoCar getInstance() {
        return instance;
    }

    public CarSet getCarHandler() {
        return handler;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
