package dev.sky_lock.pocketlifevehicle.task;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

class AbstractWarning {
    private static final String WARNING = ChatColor.RED + "⚠⚠WARNING⚠⚠";
    private int count;


    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    void send(Player player, String subTitle) {
        player.sendTitle(WARNING, subTitle, 1, 20, 1);
    }

    void stop(Player player) {
        player.resetTitle();
    }
}
