package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.SignEditor;
import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.SettingIndex;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class EditLoreContents extends SignEditor {

    public EditLoreContents() {
        super(MoCar.getInstance(), (packet, player) -> {
            List<String> lores = packet.getLines();
            lores.removeAll(Arrays.asList("", null));
            List<String> newLores = lores.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore)).collect(Collectors.toList());
            EditSessions.get(player.getUniqueId()).ifPresent(session -> {
                session.setLore(lores);
            });
            return SettingIndex.MAIN_MENU.value();
        });
    }
}
