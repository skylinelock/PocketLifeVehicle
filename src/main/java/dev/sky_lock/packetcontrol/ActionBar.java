package dev.sky_lock.packetcontrol;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ActionBar {

    public static void sendPacket(Player player, String text) {
        ChatPacket packet = new ChatPacket();
        packet.setType(EnumWrappers.ChatType.GAME_INFO);
        packet.setChatComponent(WrappedChatComponent.fromText(text));
        packet.send(player);
    }
}
