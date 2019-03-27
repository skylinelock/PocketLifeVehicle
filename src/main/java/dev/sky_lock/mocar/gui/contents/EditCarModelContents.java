package dev.sky_lock.mocar.gui.contents;

import dev.sky_lock.glassy.gui.MenuContents;
import dev.sky_lock.glassy.gui.Slot;
import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class EditCarModelContents extends MenuContents {

    private final ItemStack addCarItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name(ChatColor.GREEN + "車種を追加する").build();

    @Override
    public void open(Player player) {
        super.addSlot(new Slot(4, addCarItem, event -> {
            EditSessions.newSession(event.getWhoClicked().getUniqueId());
            new ModelSettingMenu(player).open(player);
        }));
    }
}
