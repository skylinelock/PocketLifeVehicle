package dev.sky_lock.pocketlifevehicle;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public enum Permission {
    ADMIN_COMMAND("plvehicle.command.admin.use"),
    CAR_CLICK("plvehicle.entity.open-gui"),
    CAR_PLACE("plvehicle.entity.place");

    private final String permission;
    Permission(String permission) {
        this.permission = permission;
    }

    public boolean obtained(Player player) {
        return player.hasPermission(permission);
    }

    public boolean obtained(CommandSender sender) {
        return sender.hasPermission(permission);
    }
}
