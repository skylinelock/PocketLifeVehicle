package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.sky_lock.glassy.packet.ServerPacket;

/**
 * @author sky_lock
 */

public class TitlePacket extends ServerPacket {

    public TitlePacket() {
        super(PacketType.Play.Server.TITLE);
    }

    public void setAction(EnumWrappers.TitleAction value) {
        getPacket().getTitleActions().write(0, value);
    }

    public void setTitle(WrappedChatComponent value) {
        getPacket().getChatComponents().write(0, value);
    }

    public void setFadeIn(int value) {
        getPacket().getIntegers().write(0, value);
    }

    public void setStay(int value) {
        getPacket().getIntegers().write(1, value);
    }

    public void setFadeOut(int value) {
        getPacket().getIntegers().write(2, value);
    }
}
