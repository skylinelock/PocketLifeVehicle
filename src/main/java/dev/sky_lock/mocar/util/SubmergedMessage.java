package dev.sky_lock.mocar.util;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.sky_lock.mocar.packet.TitlePacket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SubmergedMessage {
    private final TitlePacket mainTitle;
    private int count;

    public SubmergedMessage() {
        mainTitle = new TitlePacket();
        mainTitle.setTitle(WrappedChatComponent.fromText(ChatColor.RED + "⚠⚠WARNING⚠⚠"));
        mainTitle.setStay(20);
        mainTitle.setAction(EnumWrappers.TitleAction.TITLE);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void send(Player player) {
        mainTitle.send(player);
        TitlePacket subTitle = new TitlePacket();
        subTitle.setTitle(WrappedChatComponent.fromText( ChatColor.GOLD + "車は" + count + "秒後に水没します"));
        subTitle.setStay(20);
        subTitle.setAction(EnumWrappers.TitleAction.SUBTITLE);
        subTitle.send(player);
    }

    public void stop(Player player) {
        mainTitle.setTitle(WrappedChatComponent.fromText(""));
        TitlePacket subReset = new TitlePacket();
        subReset.setAction(EnumWrappers.TitleAction.SUBTITLE);
        subReset.setTitle(WrappedChatComponent.fromText(""));
        mainTitle.send(player);
        subReset.send(player);
    }
}
