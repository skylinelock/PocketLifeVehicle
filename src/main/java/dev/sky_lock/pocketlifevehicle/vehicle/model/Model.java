package dev.sky_lock.pocketlifevehicle.vehicle.model;

import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.util.TypeChecks;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sky_lock
 */

@SerializableAs("Model")
public class Model implements ConfigurationSerializable {
    private final String id;
    private final String name;
    private final List<String> lore;
    private final Spec spec;
    private final ItemOption itemOption;
    private final CollideBox collideBox;
    private final boolean isBig;
    private final float height;
    private final Sound sound;

    Model(String id, String name,
          List<String> lore, Spec spec, ItemOption itemOption,
          CollideBox collideBox, boolean isBig, float height, Sound sound) {
        this.id = id;
        this.name = name;
        this.lore = lore;
        this.spec = spec;
        this.itemOption = itemOption;
        this.isBig = isBig;
        this.collideBox = collideBox;
        this.height = height;
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

    public Spec getSpec() {
        return spec;
    }

    public ItemOption getItemOption() {
        return itemOption;
    }

    public CollideBox getCollideBox() {
        return collideBox;
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

    public ItemStack getItemStack() {
        return ItemStackBuilder.of(itemOption.getType(), 1).name(name).customModelData(itemOption.getId()).unbreakable(true).itemFlags(ItemFlag.values()).build();
    }

    public static Model deserialize(Map<String, Object> map) {
        String id = (String) map.get("id");
        String name = (String) map.get("name");
        List<String> lore;
        Object mapObj = map.get("lore");
        if (mapObj == null) {
            lore = Collections.emptyList();
        } else {
            try {
                lore = TypeChecks.checkListTypeDynamically(mapObj, String.class);
            } catch (ClassCastException ex) {
                lore = Collections.emptyList();
            }
        }
        Spec spec = (Spec) map.get("spec");
        ItemOption itemOption = (ItemOption) map.get("item");
        CollideBox collideBox = (CollideBox) map.get("collidebox");
        boolean isBig = Boolean.parseBoolean(String.valueOf(map.get("big")));
        float height = (float) ((double) map.get("height"));
        Sound sound = Sound.valueOf(String.valueOf(map.get("sound")));

        return ModelBuilder.of(id)
                .name(name)
                .lore(lore)
                .spec(spec)
                .item(itemOption)
                .collideBox(collideBox.getBaseSide(), collideBox.getHeight())
                .big(isBig)
                .height(height)
                .sound(sound)
                .build();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("lore", lore);
        map.put("spec", spec);
        map.put("item", itemOption);
        map.put("collidebox", collideBox);
        map.put("big", isBig);
        map.put("height", height);
        map.put("sound", sound.toString());

        return map;
    }

}