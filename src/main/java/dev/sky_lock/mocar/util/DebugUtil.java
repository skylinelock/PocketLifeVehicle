package dev.sky_lock.mocar.util;

import org.bukkit.Bukkit;

/**
 * @author sky_lock
 */

public class DebugUtil {

    public static void sendDebugMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }
}
