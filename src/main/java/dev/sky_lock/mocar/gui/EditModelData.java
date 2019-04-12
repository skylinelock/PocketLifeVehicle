package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.Capacity;
import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.MaxSpeed;

import java.util.List;

/**
 * @author sky_lock
 */

public class EditModelData {

    private String id;
    private String name;
    private MaxSpeed maxSpeed;
    private CarItem carItem;
    private List<String> lores;
    private Capacity capacity;
    private float fuel;

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public float getFuel() {
        return fuel;
    }

    void setLore(List<String> lores) {
        this.lores = lores;
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
