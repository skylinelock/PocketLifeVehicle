package dev.sky_lock.pocketlifevehicle.task;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

class SubmergedWarning extends AbstractWarning {

    void send(Player player) {
        super.send(player, ChatColor.GOLD + "乗り物は" + getCount() + "秒後に水没します");
    }
}