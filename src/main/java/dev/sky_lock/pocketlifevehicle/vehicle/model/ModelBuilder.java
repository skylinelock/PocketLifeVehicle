package dev.sky_lock.pocketlifevehicle.vehicle.model;

import java.util.List;

/**
 * @author sky_lock
 */

public class ModelBuilder {
    private final String id;
    private String name;
    private List<String> lore;
    private Spec spec;
    private ItemOption itemOption;
    private boolean isBig;
    private CollideBox collideBox;
    private float height;
    private Sound sound;

    public ModelBuilder(String id) {
        this.id = id;
    }

    public static ModelBuilder of(String id) {
        return new ModelBuilder(id);
    }

    public ModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ModelBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ModelBuilder spec(Spec spec) {
        this.spec = spec;
        return this;
    }

    public ModelBuilder item(ItemOption itemOption) {
        this.itemOption = itemOption;
        return this;
    }

    public ModelBuilder big(boolean isBig) {
        this.isBig = isBig;
        return this;
    }

    public ModelBuilder collideBox(float baseSide, float height) {
        this.collideBox = new CollideBox(baseSide, height);
        return this;
    }

    public ModelBuilder height(float height) {
        this.height = height;
        return this;
    }

    public ModelBuilder sound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public Model build() {
        if (name == null || spec == null || itemOption == null || collideBox == null || sound == null) {
            throw new NullPointerException();
        }
        return new Model(id, name, lore, spec, itemOption, collideBox, isBig, height, sound);
    }

}