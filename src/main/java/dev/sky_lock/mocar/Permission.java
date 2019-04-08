package dev.sky_lock.mocar;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public enum Permission {
    ADMIN_COMMAND("mocar.command.admin.use"),
    CAR_CLICK("mocar.entity.gui.open"),
    CAR_PLACE("mocar.entity.car.place");

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
