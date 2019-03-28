package dev.sky_lock.mocar.task;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import dev.sky_lock.mocar.packet.TitlePacket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class AbstractWarning {
    private final TitlePacket mainTitle;
    private int count;

    AbstractWarning() {
        mainTitle = new TitlePacket();
        mainTitle.setTitle(WrappedChatComponent.fromText(ChatColor.RED + "⚠⚠WARNING⚠⚠"));
        mainTitle.setStay(20);
        mainTitle.setAction(EnumWrappers.TitleAction.TITLE);
    }

    int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    public void send(Player player) {
        mainTitle.send(player);
    }

    void stop(Player player) {
        mainTitle.setTitle(WrappedChatComponent.fromText(""));
        TitlePacket subReset = new TitlePacket();
        subReset.setAction(EnumWrappers.TitleAction.SUBTITLE);
        subReset.setTitle(WrappedChatComponent.fromText(""));
        mainTitle.send(player);
        subReset.send(player);
    }
}
