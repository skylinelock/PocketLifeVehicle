package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.Speed;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.item.Glowing;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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

        setFrame();

        super.addComponent(new Button(4, new ItemStackBuilder(Material.ENDER_PEARL, 1).name(ChatColor.RED + "戻る").build(), (event) -> {
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

    private void setFrame() {
        Inventory inventory = getInventory();
        ItemStack white_pane = new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.WHITE).build();
        inventory.setItem(3, white_pane);
        inventory.setItem(5, white_pane);
        inventory.setItem(48, white_pane);
        inventory.setItem(50, white_pane);

        ItemStack red_pane = new ItemStackBuilder(Material.STAINED_GLASS_PANE, 1).dyeColor(DyeColor.RED).build();
        for (int i = 0; i < 54; i++) {
            if (i % 9 == 0 || i % 9 == 8) {
                inventory.setItem(i, red_pane);
            }
        }
        inventory.setItem(1, red_pane);
        inventory.setItem(2, red_pane);
        inventory.setItem(6, red_pane);
        inventory.setItem(7, red_pane);
        inventory.setItem(46, red_pane);
        inventory.setItem(47, red_pane);
        inventory.setItem(51, red_pane);
        inventory.setItem(52, red_pane);
    }

    private void setSpeedComponent() {
        ItemStack item;
        Speed speed = data.getSpeed();
        if (speed == null) {
            item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NONE").build();
        } else {
            switch (speed) {
                case SLOWEST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:SLOWEST").enchant(new Glowing(), 1).build();
                    break;
                case SLOW:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:SLOW").enchant(new Glowing(), 1).build();
                    break;
                case NORMAL:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NORMAL").enchant(new Glowing(), 1).build();
                    break;
                case FAST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:FAST").enchant(new Glowing(), 1).build();
                    break;
                case FASTEST:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:FASTEST").enchant(new Glowing(), 1).build();
                    break;
                default:
                    item = new ItemStackBuilder(Material.DIAMOND, 1).name("Speed:NONE").enchant(new Glowing(), 1).build();
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
            item = new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name(data.getId()).enchant(new Glowing(), 1).build();
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
            item = new ItemStackBuilder(Material.SIGN, 1).name("Lore").lore(data.getLores()).enchant(new Glowing(), 1).build();
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
            item = new ItemStackBuilder(Material.NAME_TAG, 1).name(data.getName()).enchant(new Glowing(), 1).build();
        }
        super.addComponent(new Button(22, item, (event) -> {
            StringEditor.open(player, StringEditor.Type.NAME);
        }));
    }

    private void setCreateComponent() {
        super.addComponent(new Button(49, new ItemStackBuilder(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build(), (event) -> {
            new ConfirmScreen(player, (event1) -> {
                ItemStack clicked = event1.getCurrentItem();
                if (data.getId() == null || data.getName() == null || data.getSpeed() == null) {
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "設定が完了していません"));
                    clicked.setItemMeta(itemMeta);
                    event1.setCurrentItem(clicked);
                    return;
                }
                if (ModelList.exists(data.getId())) {
                    ItemMeta itemMeta = clicked.getItemMeta();
                    itemMeta.setLore(Collections.singletonList(ChatColor.RED + "既に存在するIDです"));
                    clicked.setItemMeta(itemMeta);
                    event1.setCurrentItem(clicked);
                    return;
                }
                ModelList.add(new CarModel(data.getId(), data.getCarItem(), data.getName(), data.getLores(), data.getFuel(), data.getSpeed().ordinal()));
                player.sendMessage(MoCar.PREFIX + "新しい車種を追加しました");
                EditSessions.destroy(player.getUniqueId());
                player.closeInventory();
            }).open(player);
        }));
    }
}
