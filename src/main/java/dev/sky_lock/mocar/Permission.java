package dev.sky_lock.mocar;

import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public enum Permission {
    ADMIN_COMMAND("mocar.command.admin.use"),
    CAR_CLICK("mocar.entity.gui.open");

    private final String permisson;
    Permission(String permission) {
        this.permisson = permission;
    }

    public boolean obtained(Player player) {
        return player.hasPermission(permisson);
    }
}
