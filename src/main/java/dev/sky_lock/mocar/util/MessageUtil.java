package dev.sky_lock.mocar.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author sky_lock
 */

public class MessageUtil {

    public static void sendDebugMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public static String attachColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
