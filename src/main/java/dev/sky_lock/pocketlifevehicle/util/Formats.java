package dev.sky_lock.pocketlifevehicle.util;

import org.bukkit.ChatColor;

/**
 * @author sky_lock
 */

public class Formats {

    public static String truncateToOneDecimalPlace(float f) {
        return String.format("%.1f", f);
    }

    public static String removeBlanks(String blankText) {
        return blankText.replaceAll("\\s", "");
    }

    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
