package dev.sky_lock.mocar.util;

import org.bukkit.ChatColor;

/**
 * @author sky_lock
 */

public class MessageUtil {

    public static String attachColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
