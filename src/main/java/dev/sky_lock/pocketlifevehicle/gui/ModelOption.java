package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.pocketlifevehicle.vehicle.model.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky_lock
 */

public class ModelOption {

    private boolean justEditing;

    private String id;
    private String name;
    private List<String> lore;

    private MaxSpeed maxSpeed;
    private float maxFuel;
    private Capacity capacity;
    private SteeringLevel steeringLevel;

    private Material itemType;
    private int itemId;
    private ItemPosition position;

    private float collideBaseSide;
    private float collideHeight;

    private boolean isBig;

    private float height;

    private Sound sound;

    public boolean isJustEditing() {
        return justEditing;
    }

    public void setJustEditing(boolean editing) {
        this.justEditing = editing;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setMaxSpeed(MaxSpeed maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMaxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public void setSteeringLevel(SteeringLevel steeringLevel) {
        this.steeringLevel = steeringLevel;
    }

    public void setItemType(Material type) {
        this.itemType = type;
    }

    public void setItemID(int itemId) {
        this.itemId = itemId;
    }

    public void setItemPosition(ItemPosition position) {
        this.position = position;
    }

    public void setCollideBaseSide(float baseSide) {
        this.collideBaseSide = baseSide;
    }

    public void setCollideHeight(float height) {
        this.collideHeight = height;
    }

    public void setBig(boolean isBig) {
        this.isBig = isBig;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public MaxSpeed getMaxSpeed() {
        return maxSpeed;
    }

    public float getMaxFuel() {
        return maxFuel;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public SteeringLevel getSteeringLevel() {
        return steeringLevel;
    }

    public Material getItemType() {
        return itemType;
    }

    public int getItemId() {
        return itemId;
    }

    public ItemPosition getPosition() {
        return position;
    }

    public float getCollideBaseSide() {
        return this.collideBaseSide;
    }

    public float getCollideHeight() {
        return collideHeight;
    }

    public boolean isBig() {
        return isBig;
    }

    public float getHeight() {
        return height;
    }

    public Sound getSound() {
        return sound;
    }

    public Model generate() {
        return ModelBuilder.of(id)
                .name(name)
                .lore(lore)
                .spec(new Spec(maxFuel, maxSpeed, capacity, SteeringLevel.NORMAL))
                .item(new ItemOption(itemType, itemId, position))
                .collideBox(collideBaseSide, collideHeight)
                .big(isBig)
                .height(height)
                .sound(Sound.NONE)
                .build();
    }

    public boolean verifyCompleted() {
        return id != null &&
                name != null &&
                maxFuel != 0.0F &&
                maxSpeed != null &&
                capacity != null &&
                //steeringLevel != null &&
                (itemType != null && itemId != 0) &&
                position != null &&
                height != 0.0F;
                //sound != null;
    }

    public List<String> unfilledOptionWarning() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "設定が完了していません");
        lore.add(ChatColor.RED + "必須項目");
        if (id == null) {
            lore.add(ChatColor.RED + "- ID");
        }
        if (name == null) {
            lore.add(ChatColor.RED + "- 名前");
        }
        if (maxFuel == 0.0F) {
            lore.add(ChatColor.RED + "- 燃料上限");
        }
        if (maxSpeed == null) {
            lore.add(ChatColor.RED + "- 最高速度");
        }
        if (capacity == null) {
            lore.add(ChatColor.RED + "- 乗車人数");
        }
//        if (steeringLevel == null) {
//            lore.add(ChatColor.RED + "- ステアリング感度");
//        }
        if (itemType == null || itemId == 0) {
            lore.add(ChatColor.RED + "- アイテム");
        }
        if (position == null) {
            lore.add(ChatColor.RED + "- アイテム位置");
        }
        if (height == 0.0F) {
            lore.add(ChatColor.RED + "- 座高");
        }
//        if (sound == null) {
//            lore.add(ChatColor.RED + "- サウンド");
//        }
        return lore;
    }
}
