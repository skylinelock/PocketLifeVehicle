package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.car.CarModel;
import dev.sky_lock.mocar.car.ModelList;
import dev.sky_lock.mocar.car.Speed;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
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
            close(player);
            new CarModelEditor(player).open((Player) event.getWhoClicked());
        }));

        setIdComponent();
        setNameComponent();
        setSpeedComponent();

        super.addComponent(new Button(29, new ItemStackBuilder(Material.RAILS, 1).name("LORE").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(31, new ItemStackBuilder(Material.SLIME_BALL, 1).name("ITEM").build(), (event) -> {
            //TODO;
        }));
        super.addComponent(new Button(33, new ItemStackBuilder(Material.COAL_BLOCK, 1).name("FUEL").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(49, new ItemStackBuilder(Material.END_CRYSTAL, 1).name("CREATE").build(), (event) -> {
            new ConfirmScreen(player, (event1) -> {
               if (data.getId() != null && data.getSpeed() != null) {
                   ModelList.add(new CarModel(data.getId(), data.getCarItem(), data.getName(), data.getLores(), data.getFuel(), data.getSpeed().ordinal()));
                   close(player);
                   player.sendMessage(MoCar.PREFIX + "新しい車種を追加しました");
                   return;
               }
               ItemStack yesItem = getInventory().getItem(49);
               ItemMeta itemMeta = yesItem.getItemMeta();
               itemMeta.setLore(Collections.singletonList(ChatColor.RED + "設定が完了していません"));
               yesItem.setItemMeta(itemMeta);
               player.updateInventory();
            }).open(player);
        }));
    }

    @Override
    public void close(Player player) {
        super.close(player);
        EditSessions.destroy(player.getUniqueId());
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
        if (data.getId() == null || data.getId().equalsIgnoreCase("ID")) {
            item = new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name("Id").build();
        } else {
            item = new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name(data.getId()).build();
        }
        super.addComponent(new Button(20, item, (event) -> {
            StringEditor.open(player, StringEditor.Type.ID);
        }));
    }

    private void setNameComponent() {
        ItemStack item;
        if (data.getName() == null || data.getName().equalsIgnoreCase("NAME")) {
            item = new ItemStackBuilder(Material.NAME_TAG, 1).name("Name").build();
        } else {
            item = new ItemStackBuilder(Material.NAME_TAG, 1).name(data.getName()).build();
        }
        super.addComponent(new Button(22, item, (event) -> {
            StringEditor.open(player, StringEditor.Type.NAME);
        }));
    }
}