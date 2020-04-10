package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author sky_lock
 */

public class EditCarModelContents extends MenuContents {

    private final ItemStack carItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name(ChatColor.GREEN + "車種を追加する").build();

    public EditCarModelContents(Player player) {
        EditSessions.newSession(player.getUniqueId());
        super.addSlot(new Slot(4, carItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> {
                menu.flip(player, ModelMenuIndex.SETTING.value());
            });
        }));
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

}
