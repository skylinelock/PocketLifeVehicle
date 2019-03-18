package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.Speed;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.util.DebugUtil;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

/**
 * @author sky_lock
 */

public class ModelSetting extends GuiWindow {
    private final Player player;
    private final EditModelData data;

    public ModelSetting(Player player) {
        super("AddModel", player, GuiType.WIDE);
        this.player = player;
        this.data = EditSessions.get(player.getUniqueId());

        super.addComponent(new Button(4, new ItemStackBuilder(Material.ENDER_PEARL, 1).name("戻る").build(), (event) -> {
            new CarModelEditor(player).open((Player) event.getWhoClicked());
            EditSessions.destroy(player.getUniqueId());
        }));

        setIdComponent();
        setNameComponent();
        setSpeedComponent();
        setLoreComponent();

        super.addComponent(new Button(31, new ItemStackBuilder(Material.SLIME_BALL, 1).name("Item").build(), (event) -> {
            //TODO;
        }));
        super.addComponent(new Button(33, new ItemStackBuilder(Material.COAL_BLOCK, 1).name("Fuel").build(), (event) -> {
            //TODO:
        }));

        setCreateComponent();
    }

    private void setSpeedComponent() {
        ItemStack item;
        Speed speed = data.getSpeed();
        if (speed == null) {
            item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NONE").build();
        } else {
            switch (speed) {
                case SLOWEST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:SLOWEST").build();
                    break;
                case SLOW:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:SLOW").build();
                    break;
                case NORMAL:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NORMAL").build();
                    break;
                case FAST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:FAST").build();
                    break;
                case FASTEST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:FASTEST").build();
                    break;
                default:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NONE").build();
            }
        }
        super.addComponent(new Button(24, item, (event) -> {
            new SpeedSelector(player).open(player);
        }));
    }

    private void setIdComponent() {
        ItemStack item;
        if (data.getId() == null) {
            item = new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name("Id").build();
        } else {
            item = new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name(data.getId()).build();
        }
        super.addComponent(new Button(20, item, (event) -> {
            StringEditor.open(player, StringEditor.Type.ID);
        }));
    }

    private void setLoreComponent() {
        ItemStack item;
        if (data.getLores() == null) {
            item = new ItemStackBuilder(Material.SIGN, 1).name("Lore").build();
        } else {
            item = new ItemStackBuilder(Material.SIGN, 1).name("Lore").lore(data.getLores()).build();
        }
        super.addComponent(new Button(29, item, (event) -> {
            new SignEditor().open(player);
        }));
    }

    private void setNameComponent() {
        ItemStack item;
        if (data.getName() == null) {
            item = new ItemStackBuilder(Material.NAME_TAG, 1).name("Name").build();
        } else {
            item = new ItemStackBuilder(Material.NAME_TAG, 1).name(data.getName()).build();
        }
        super.addComponent(new Button(22, item, (event) -> {
            StringEditor.open(player, StringEditor.Type.NAME);
        }));
    }

    private void setCreateComponent() {
        super.addComponent(new Button(49, new ItemStackBuilder(Material.END_CRYSTAL, 1).name("追加する").build(), (event) -> {
            new ConfirmScreen(player, (event1) -> {
                if (data.getId() == null || data.getName() == null || data.getSpeed() == null) {
                    DebugUtil.sendDebugMessage(data.getId() + data.getSpeed());
                    ItemStack yesItem = event1.getCurrentItem();
                    ItemMeta itemMeta = yesItem.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "設定が完了していません"));
                    yesItem.setItemMeta(itemMeta);
                    event1.setCurrentItem(yesItem);
                    return;
                }
                DebugUtil.sendDebugMessage(data.getId() + " : " + data.getSpeed());
                ModelList.add(new CarModel(data.getId(), data.getCarItem(), data.getName(), data.getLores(), data.getFuel(), data.getSpeed().ordinal()));
                player.sendMessage(MoCar.PREFIX + "新しい車種を追加しました");
                EditSessions.destroy(player.getUniqueId());
                player.closeInventory();
            }).open(player);
        }));
    }
}
