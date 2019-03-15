package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.CarItem;
import dev.sky_lock.mocar.car.Speed;

import java.util.List;

/**
 * @author sky_lock
 */

public class EditModelData {

    private String id;
    private String name;
    private Speed speed;
    private CarItem carItem;
    private List<String> lores;
    private float fuel;

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public float getFuel() {
        return fuel;
    }

    public void setLores(List<String> lores) {
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

    public Speed getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
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

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }
}
