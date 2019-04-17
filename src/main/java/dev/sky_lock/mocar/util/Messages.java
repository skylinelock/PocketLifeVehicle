package dev.sky_lock.mocar.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author sky_lock
 */

public class Messages {

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void dispatchDebug(String debugText) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(debugText));
    }

}
