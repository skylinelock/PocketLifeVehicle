package dev.sky_lock.pocketlifevehicle.gui.contents;

import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.gui.EditSessions;
import dev.sky_lock.pocketlifevehicle.gui.ModelMenuIndex;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import dev.sky_lock.pocketlifevehicle.vehicle.model.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
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
        for (Model model : ModelList.unmodified()) {
            if (modelSlot > 44) {
                break;
            }
            String name = ChatColor.YELLOW + model.getName();
            List<String> desc = new ArrayList<>();
            desc.add(ChatColor.DARK_AQUA + "ID: " + ChatColor.AQUA + model.getId());
            desc.add(ChatColor.DARK_AQUA + "名前: " + ChatColor.AQUA + model.getName());
            //TODO: [] -> ""
            List<String> lore = model.getLore();
            if (lore == null || lore.isEmpty()) {
                lore = Collections.singletonList("");
            }
            desc.add(ChatColor.DARK_AQUA + "説明: " + ChatColor.AQUA + lore);
            Spec spec = model.getSpec();
            desc.add(ChatColor.DARK_AQUA + "燃料上限: " + ChatColor.AQUA + spec.getMaxFuel());
            desc.add(ChatColor.DARK_AQUA + "最高速度: " + ChatColor.AQUA + spec.getMaxSpeed().getLabel());
            desc.add(ChatColor.DARK_AQUA + "乗車人数: " + ChatColor.AQUA + spec.getCapacity().value());
            ItemOption itemOption = model.getItemOption();
            CollideBox box = model.getCollideBox();
            desc.add(ChatColor.DARK_AQUA + "モデル位置: " + ChatColor.AQUA + itemOption.getPosition().getLabel());
            desc.add(ChatColor.DARK_AQUA + "当たり判定: " + ChatColor.AQUA + box.getBaseSide() + "×" + box.getHeight());
            String size = model.isBig() ? "大きい" : "小さい";
            desc.add(ChatColor.DARK_AQUA + "大きさ: " + ChatColor.AQUA + size);
            desc.add(ChatColor.DARK_AQUA + "座高: " + ChatColor.AQUA + Formats.truncateToOneDecimalPlace(model.getHeight()));
            ItemStack item = ItemStackBuilder.of(model.getItemStack()).name(name).lore(desc).build();
            super.addSlot(new Slot(modelSlot, item, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                        session.setJustEditing(true);

                        session.setId(model.getId());
                        session.setName(model.getName());
                        session.setLore(model.getLore());

                        session.setMaxFuel(spec.getMaxFuel());
                        session.setMaxSpeed(spec.getMaxSpeed());
                        session.setCapacity(spec.getCapacity());

                        session.setItemType(itemOption.getType());
                        session.setItemID(itemOption.getId());
                        session.setItemPosition(itemOption.getPosition());

                        session.setCollideBaseSide(model.getCollideBox().getBaseSide());
                        session.setCollideHeight(model.getCollideBox().getHeight());

                        session.setBig(model.isBig());
                        session.setHeight(model.getHeight());
                        session.setSound(model.getSound());
                        menu.flip(player, ModelMenuIndex.SETTING.ordinal());
                    });
                });
            }));
            modelSlot++;
        }
        super.addSlot(new Slot(49, carItem, event -> {
            InventoryMenu.of(player).ifPresent(menu -> {
                EditSessions.of(player.getUniqueId()).ifPresent(session -> {
                    session.setJustEditing(false);
                    menu.flip(player, ModelMenuIndex.SETTING.ordinal());
                });
            });
        }));
    }

    @Override
    public void onFlip(InventoryMenu inventoryMenu) {

    }

}
