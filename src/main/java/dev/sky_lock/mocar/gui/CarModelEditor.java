package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class CarModelEditor extends GuiWindow {
    public static final List<GuiWindow> windows = new ArrayList<>();

    public CarModelEditor(Player player) {
        super("ModelEditor", player, GuiType.BIG);
        super.addComponent(new Button(4, new ItemStackBuilder(Material.STORAGE_MINECART, 1).name(ChatColor.GREEN + "車種を追加する").build(), (event) -> {
            EditSessions.newSession(player.getUniqueId());
            new ModelSetting(player).open((Player) event.getWhoClicked());
        }));
    }
}
