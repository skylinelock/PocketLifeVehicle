package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelItem;
import dev.sky_lock.pocketlifevehicle.vehicle.model.MaxSpeed;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelPosition;

import java.util.List;

/**
 * @author sky_lock
 */

public class EditModelData {

    private boolean justEditing;
    private String id;
    private String name;
    private MaxSpeed maxSpeed;
    private ModelItem modelItem;
    private List<String> lore;
    private Capacity capacity;
    private float fuel;
    private float collideBaseSide;
    private float collideHeight;
    private float height;
    private ModelPosition position;

    public float getCollideBaseSide() {
        return this.collideBaseSide;
    }

    public void setCollideBaseSide(float baseSide) {
        this.collideBaseSide = baseSide;
    }

    public boolean isJustEditing() {
        return justEditing;
    }

    public void setJustEditing(boolean editing) {
        this.justEditing = editing;
    }

    public float getCollideHeight() {
        return collideHeight;
    }

    public void setCollideHeight(float height) {
        this.collideHeight = height;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public float getFuel() {
        return fuel;
    }

    public float getHeight() {
        return height;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setPosition(ModelPosition position) {
        this.position = position;
    }

    public List<String> getLore() {
        return lore;
    }

    public ModelItem getModelItem() {
        return modelItem;
    }

    public String getId() {
        return id;
    }

    public MaxSpeed getMaxSpeed() {
        return maxSpeed;
    }

    public String getName() {
        return name;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public ModelPosition getPosition() {
        return position;
    }

    public void setModelItem(ModelItem modelItem) {
        this.modelItem = modelItem;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxSpeed(MaxSpeed maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }
}
