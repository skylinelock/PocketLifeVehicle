package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.sky_lock.mocar.MoCar;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * @author sky_lock
 */

public class ActionBar {

    public static void sendPacket(Player player, String text) {
        try {
            PacketContainer chat = MoCar.getInstance().getProtocolManager().createPacket(PacketType.Play.Server.CHAT);
            chat.getChatComponents().write(0, WrappedChatComponent.fromText(text));
            chat.getChatTypes().write(0, EnumWrappers.ChatType.GAME_INFO);

            MoCar.getInstance().getProtocolManager().sendServerPacket(player, chat);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
