package dev.sky_lock.mocar.click;

import dev.sky_lock.mocar.gui.EditSessions;
import dev.sky_lock.mocar.gui.ModelSettingMenu;
import dev.sky_lock.mocar.gui.StringEditor;
import dev.sky_lock.mocar.util.MessageUtil;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;

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
        if (itemStack.hasTag() && itemStack.getTag().hasKey("editor-result")) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
            org.bukkit.inventory.ItemStack result = event.getCurrentItem();

            StringEditor editor = StringEditor.get((Player) event.getWhoClicked());
            EditSessions.get(event.getWhoClicked().getUniqueId()).ifPresent(session -> {
                if (editor.getEditorType() == StringEditor.Type.ID) {
                    session.setId(result.getItemMeta().getDisplayName());
                } else if (editor.getEditorType() == StringEditor.Type.NAME) {
                    String name = MessageUtil.attachColor(result.getItemMeta().getDisplayName());
                    session.setName(name);
                }
                Player player = (Player) event.getWhoClicked();
                StringEditor.close(player);
                new ModelSettingMenu(player).open(player);
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
