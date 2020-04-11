package dev.sky_lock.pocketlifevehicle.gui.contents;

import com.google.common.collect.ImmutableList;
import dev.sky_lock.menu.InventoryMenu;
import dev.sky_lock.menu.MenuContents;
import dev.sky_lock.menu.Slot;
import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.car.CarModelBuilder;
import dev.sky_lock.pocketlifevehicle.car.CarSound;
import dev.sky_lock.pocketlifevehicle.car.ModelList;
import dev.sky_lock.pocketlifevehicle.car.SteeringLevel;
import dev.sky_lock.pocketlifevehicle.gui.*;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author sky_lock
 */

public class ModelSettingContents extends MenuContents {
    private final Player player;

    private ItemStack carItem = ItemStackBuilder.of(Material.ITEM_FRAME, 1).name("アイテム").build();
    private ItemStack capacityItem = ItemStackBuilder.of(Material.SADDLE, 1).name("乗車人数").build();
    private ItemStack fuelItem = ItemStackBuilder.of(Material.COAL_BLOCK, 1).name("燃料上限").build();
    private ItemStack idItem = ItemStackBuilder.of(Material.EMERALD, 1).name("ID").build();
    private ItemStack nameItem = ItemStackBuilder.of(Material.NAME_TAG, 1).name("名前").build();
    private ItemStack speedItem = ItemStackBuilder.of(Material.DIAMOND, 1).name("最高速度").build();
    private ItemStack loreItem = ItemStackBuilder.of(Material.OAK_SIGN, 1).name("説明").build();
    private ItemStack collideItem = ItemStackBuilder.of(Material.BEACON, 1).name(ChatColor.RESET + "当たり判定").build();
    private ItemStack heightItem = ItemStackBuilder.of(Material.PRISMARINE_STAIRS, 1).name("座高").build();
    private ItemStack soundItem = ItemStackBuilder.of(Material.BELL, 1).name("エンジン音").lore(ImmutableList.of(ChatColor.RED + "Coming soon")).build();
    private ItemStack steeringItem = ItemStackBuilder.of(Material.LEAD, 1).name("ステアリング").lore(ImmutableList.of(ChatColor.RED + "Coming soon")).build();
    private ItemStack equipOnHandItem = ItemStackBuilder.of(Material.SCUTE, 1).name("モデルのアイテム位置").lore(ImmutableList.of(ChatColor.RED + "Coming soon")).build();

    private ItemStack updateItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "更新する").build();
    private ItemStack removeItem = ItemStackBuilder.of(Material.BARRIER, 1).name(ChatColor.RED + "削除する").build();
    private ItemStack createItem = ItemStackBuilder.of(Material.END_CRYSTAL, 1).name(ChatColor.GREEN + "追加する").build();


    public ModelSettingContents(Player player) {
        this.player = player;
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            this.addSlot(new Slot(4, ItemStackBuilder.of(Material.ENDER_EYE, 1).name(ChatColor.RED + "閉じる").build(), event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.close(player));
                EditSessions.destroy(player.getUniqueId());
            }));

            this.addSlot(new Slot(13, nameItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    StringEditor.open(player, StringEditor.Type.NAME, (ModelSettingMenu) menu);
                });
            }));

            this.addSlot(new Slot(15, speedItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> {
                    menu.flip(player, ModelMenuIndex.SPEED.value());
                });
            }));

            this.addSlot(new Slot(20, loreItem, event -> {
                new LoreEditor(player).open();
            }));

            this.addSlot(new Slot(22, carItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAR_ITEM.value()));
            }));

            this.addSlot(new Slot(24, capacityItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.CAPACITY.value()));
            }));

            this.addSlot(new Slot(29, fuelItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.FUEL.value()));
            }));

            this.addSlot(new Slot(31, collideItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.COLLIDE_BOX.value()));
            }));

            this.addSlot(new Slot(33, heightItem, event -> {
                InventoryMenu.of(player).ifPresent(menu -> menu.flip(player, ModelMenuIndex.HEIGHT.value()));
            }));

            this.addSlot(new Slot(38, soundItem, event -> {

            }));

            this.addSlot(new Slot(40, steeringItem, event -> {

            }));

            this.addSlot(new Slot(42, equipOnHandItem, event -> {

            }));
        });
    }

    @Override
    public void onFlip(InventoryMenu menu) {
        EditSessions.of(player.getUniqueId()).ifPresent(session -> {
            if (session.isJustEditing()) {
                this.addSlot(new Slot(46, removeItem, event -> {
                    String id = session.getId();
                    if (id == null || id.equals("")) {
                        return;
                    }
                    ModelList.remove(id);
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + id + "を削除しました");
                    EditSessions.destroy(player.getUniqueId());
                    menu.close(player);
                }));
                this.addSlot(new Slot(49, updateItem, event -> {
                    ModelList.remove(session.getId());
                    ModelList.add(CarModelBuilder.of(session.getId())
                            .name(session.getName())
                            .capacity(session.getCapacity())
                            .height(session.getHeight())
                            .collideBox(session.getCollideBaseSide(), session.getCollideHeight())
                            .maxFuel(session.getFuel())
                            .maxSpeed(session.getMaxSpeed())
                            .item(session.getCarItem())
                            .sound(CarSound.NONE)
                            .steering(SteeringLevel.NORMAL)
                            .lores(session.getLores())
                            .build()
                    );
                    player.sendMessage(PLVehicle.PREFIX + ChatColor.GREEN + session.getId() + "を更新しました");
                    EditSessions.destroy(player.getUniqueId());
                    menu.close(player);
                }));
            } else {
                this.addSlot(new Slot(11, idItem, event -> {
                    InventoryMenu.of(player).ifPresent(m -> {
                        StringEditor.open(player, StringEditor.Type.ID, (ModelSettingMenu) menu);
                    });
                }));
                this.addSlot(new Slot(49, createItem, event -> {
                    InventoryMenu.of(player).ifPresent(m -> m.flip(player, ModelMenuIndex.CONFIRM.value()));
                }));
            }
            if (session.getCarItem() != null) {
                int modelId = session.getCarItem().getModelId();
                List<String> details = ImmutableList.of(session.getCarItem().getType().name(), String.valueOf(modelId));
                carItem = ItemStackBuilder.of(carItem).lore(details).grow().build();
                updateItemStack(22, carItem);
            }
            if (session.getCapacity() != null) {
                capacityItem = ItemStackBuilder.of(capacityItem).lore(ImmutableList.of(String.valueOf(session.getCapacity().value()))).grow().build();
                updateItemStack(24, capacityItem);
            }
            if (session.getFuel() != 0.0F) {
                fuelItem = ItemStackBuilder.of(fuelItem).lore(ImmutableList.of(String.valueOf(session.getFuel()))).grow().build();
                updateItemStack(29, fuelItem);
            }
            if (session.getId() != null && !session.getId().equalsIgnoreCase("id")) {
                idItem = ItemStackBuilder.of(idItem).lore(ImmutableList.of(session.getId())).grow().build();
                updateItemStack(11, idItem);
            }
            if (session.getName() != null && !session.getName().equalsIgnoreCase("name")) {
                nameItem = ItemStackBuilder.of(nameItem).lore(ImmutableList.of(session.getName())).grow().build();
                updateItemStack(13, nameItem);
            }
            if (session.getMaxSpeed() != null) {
                speedItem = ItemStackBuilder.of(speedItem).lore(ImmutableList.of(session.getMaxSpeed().getLabel())).grow().build();
                updateItemStack(15, speedItem);
            }
            if (session.getLores() != null) {
                loreItem = ItemStackBuilder.of(loreItem).lore(session.getLores()).grow().build();
                updateItemStack(20, loreItem);
            }
            if (session.getCollideHeight() != 0.0F && session.getCollideBaseSide() != 0.0F) {
                collideItem = ItemStackBuilder.of(collideItem).lore(ImmutableList.of(String.valueOf(session.getCollideBaseSide()), String.valueOf(session.getCollideHeight()))).grow().build();
                updateItemStack(31, collideItem);
            }
            if (session.getHeight() != 0.0F) {
                heightItem = ItemStackBuilder.of(heightItem).lore(ImmutableList.of(Formats.truncateToOneDecimalPlace(session.getHeight()))).grow().build();
                updateItemStack(33, heightItem);
            }

            menu.update();
        });
    }

}
