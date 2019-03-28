package dev.sky_lock.mocar.task;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.sky_lock.mocar.packet.TitlePacket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class BurnExplosionWarning extends AbstractWarning {
    public void send(Player player) {
        super.send(player);
        TitlePacket subTitle = new TitlePacket();
        subTitle.setTitle(WrappedChatComponent.fromText( ChatColor.GOLD + "乗り物は" + getCount() + "秒後に爆発します"));
        subTitle.setStay(20);
        subTitle.setAction(EnumWrappers.TitleAction.SUBTITLE);
        subTitle.send(player);
    }
}
