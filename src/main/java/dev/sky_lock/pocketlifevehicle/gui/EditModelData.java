package dev.sky_lock.pocketlifevehicle.gui;

import dev.sky_lock.pocketlifevehicle.car.Capacity;
import dev.sky_lock.pocketlifevehicle.car.CarItem;
import dev.sky_lock.pocketlifevehicle.car.MaxSpeed;

import java.util.List;

/**
 * @author sky_lock
 */

public class EditModelData {

    private boolean justEditing;
    private String id;
    private String name;
    private MaxSpeed maxSpeed;
    private CarItem carItem;
    private List<String> lores;
    private Capacity capacity;
    private float fuel;
    private float baseSide;
    private float collideHeight;
    private float height = -1;

    public float getCollideBaseSide() {
        return baseSide;
    }

    public void setCollideBaseSide(float baseSide) {
        this.baseSide = baseSide;
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

    public void setLore(List<String> lores) {
        this.lores = lores;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<String> getLores() {
        return lores;
    }

    public CarItem getCarItem() {
        return carItem;
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

    public void setCarItem(CarItem carItem) {
        this.carItem = carItem;
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
