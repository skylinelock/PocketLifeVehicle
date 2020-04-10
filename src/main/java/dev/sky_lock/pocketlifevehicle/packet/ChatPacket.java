package dev.sky_lock.pocketlifevehicle.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * @author sky_lock
 */

public class ChatPacket extends ServerPacket {

    ChatPacket() {
        super(PacketType.Play.Server.CHAT);
    }

    public void setType(EnumWrappers.ChatType type) {
        getPacket().getChatTypes().write(0, type);
    }

    void setChatComponent(WrappedChatComponent chatComponent) {
        getPacket().getChatComponents().write(0, chatComponent);
    }
}
