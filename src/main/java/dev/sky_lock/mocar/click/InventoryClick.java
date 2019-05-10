package dev.sky_lock.mocar.click;

import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelMenuIndex;
import dev.sky_lock.mocar.gui.StringEditor;
import dev.sky_lock.mocar.util.Formats;
import net.minecraft.server.v1_13_R2.ItemStack;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * @author sky_lock
 */

public class InventoryClick {
    private final InventoryClickEvent event;

    public InventoryClick(InventoryClickEvent event) {
        this.event = event;
    }

    public void accept() {
        ItemStack itemStack = CraftItemStack.asNMSCopy(event.getCurrentItem());
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            return;
        }
        if (itemStack.hasTag() && itemStack.getTag().hasKey("editor-result")) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            org.bukkit.inventory.ItemStack result = event.getCurrentItem();

            StringEditor editor = StringEditor.get((Player) event.getWhoClicked());
            EditSessions.of(event.getWhoClicked().getUniqueId()).ifPresent(session -> {
                if (editor.getEditorType() == StringEditor.Type.ID) {
                    session.setId(result.getItemMeta().getDisplayName());
                } else if (editor.getEditorType() == StringEditor.Type.NAME) {
                    String name = Formats.colorize(result.getItemMeta().getDisplayName());
                    session.setName(name);
                }
                Player player = (Player) event.getWhoClicked();
                StringEditor.get(player).getMenu().open(player, ModelMenuIndex.SETTING.value());
                StringEditor.close(player);
            });

        }
        if (!StringEditor.isOpening((Player) event.getWhoClicked())) {
            return;
        }
        if (!event.getClickedInventory().equals(event.getInventory())) {
            return;
        }
        event.setResult(Event.Result.DENY);
        event.setCancelled(true);
    }
}
