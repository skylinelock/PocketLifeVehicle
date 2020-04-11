package dev.sky_lock.pocketlifevehicle.car;

import java.util.List;

/**
 * @author sky_lock
 */

public class CarModelBuilder {
    private final String id;
    private String name;
    private List<String> lores;
    private float maxFuel;
    private MaxSpeed maxSpeed;
    private CarItem item;
    private Capacity capacity;
    private CollideBox collideBox;
    private float height;
    private SteeringLevel steeringLevel;
    private CarSound sound;
    private ModelPosition position;

    public CarModelBuilder(String id) {
        this.id = id;
    }

    public static CarModelBuilder of(String id) {
        return new CarModelBuilder(id);
    }

    public CarModelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CarModelBuilder item(CarItem item) {
        this.item = item;
        return this;
    }

    public CarModelBuilder lores(List<String> lores) {
        this.lores = lores;
        return this;
    }

    public CarModelBuilder maxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
        return this;
    }

    public CarModelBuilder maxSpeed(MaxSpeed maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public CarModelBuilder capacity(Capacity capacity) {
        this.capacity = capacity;
        return this;
    }

    public CarModelBuilder collideBox(float baseSide, float height) {
        this.collideBox = new CollideBox(baseSide, height);
        return this;
    }

    public CarModelBuilder height(float height) {
        this.height = height;
        return this;
    }

    public CarModelBuilder steering(SteeringLevel steeringLevel) {
        this.steeringLevel = steeringLevel;
        return this;
    }

    public CarModelBuilder sound(CarSound sound) {
        this.sound = sound;
        return this;
    }

    public CarModelBuilder modelPosition(ModelPosition position) {
        this.position = position;
        return this;
    }

    public CarModel build() {
        if (name == null || lores == null || maxSpeed == null ||
                item == null || capacity == null || collideBox == null ||
                steeringLevel == null || sound == null || position == null) {
            throw new NullPointerException();
        }
        return new CarModel(id, item, name, lores, maxFuel, maxSpeed, capacity, steeringLevel, collideBox, height, sound, position);
    }

}
