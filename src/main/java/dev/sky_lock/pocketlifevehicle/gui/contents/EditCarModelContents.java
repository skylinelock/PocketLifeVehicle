package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.car.CarModel;
import dev.sky_lock.pocketlifevehicle.car.CollideBox;
import dev.sky_lock.pocketlifevehicle.car.ModelList;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class EditCarModelContents extends MenuContents {

    private final ItemStack carItem = ItemStackBuilder.of(Material.CHEST_MINECART, 1).name(ChatColor.GREEN + "車種を追加する").build();

    public EditCarModelContents(Player player) {
        EditSessions.newSession(player.getUniqueId());
        super.addSlot(new Slot(4, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
            InventoryMenu.of(player).ifPresent(menu -> menu.close((Player) event.getWhoClicked()));
        }));

        int modelSlot = 9;
        for (CarModel model : ModelList.unmodified()) {
            if (modelSlot > 44) {
                break;
            }
            String name = ChatColor.YELLOW + model.getName();
            List<String> lores = new ArrayList<>();
            lores.add(ChatColor.DARK_AQUA + "ID: " + ChatColor.AQUA + model.getId());
            lores.add(ChatColor.DARK_AQUA + "名前: " + ChatColor.AQUA + model.getName());
            lores.add(ChatColor.DARK_AQUA + "説明: " + ChatColor.AQUA + model.getLores());
            lores.add(ChatColor.DARK_AQUA + "最高速度: " + ChatColor.AQUA + model.getMaxSpeed().getLabel());
            lores.add(ChatColor.DARK_AQUA + "燃料上限: " + ChatColor.AQUA + model.getMaxFuel());
            lores.add(ChatColor.DARK_AQUA + "乗車人数: " + ChatColor.AQUA + model.getCapacity().value());
            CollideBox box = model.getCollideBox();
            lores.add(ChatColor.DARK_AQUA + "当たり判定: " + ChatColor.AQUA + box.getBaseSide() + "×" + box.getHeight());
            lores.add(ChatColor.DARK_AQUA + "座高: " + ChatColor.AQUA + model.getHeight());
            ItemStack modelItem = ItemStackBuilder.of(model.getItemStack()).name(name).lore(lores).build();
            super.addSlot(new Slot(modelSlot, modelItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                        session.setJustEditing(true);
                        session.setId(model.getId());
                        session.setName(model.getName());
                        session.setLore(model.getLores());
                        session.setCollideBaseSide(model.getCollideBox().getBaseSide());
                        session.setCollideHeight(model.getCollideBox().getHeight());
                        session.setFuel(model.getMaxFuel());
                        session.setMaxSpeed(model.getMaxSpeed());
                        session.setHeight(model.getHeight());
                        session.setCapacity(model.getCapacity());
                        session.setCarItem(model.getCarItem());
                        menu.flip(player, ModelMenuIndex.SETTING.value());
                    });
                });
            }));
            modelSlot++;
        }
        super.addSlot(new Slot(49, carItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> {
                EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                    session.setJustEditing(false);
                    menu.flip(player, ModelMenuIndex.SETTING.value());
                });
            });
        }));
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

}
