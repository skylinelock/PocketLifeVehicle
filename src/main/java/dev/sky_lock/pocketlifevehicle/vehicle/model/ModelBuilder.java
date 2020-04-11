package dev.sky_lock.pocketlifevehicle.vehicle.model;

import java.util.List;

/**
 * @author sky_lock
 */

public class ModelBuilder {
    private final String id;
    private String name;
    private List<String> lores;
    private float maxFuel;
    private MaxSpeed maxSpeed;
    private ModelItem item;
    private Capacity capacity;
    private CollideBox collideBox;
    private float height;
    private SteeringLevel steeringLevel;
    private Sound sound;
    private ModelPosition position;

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

    public ModelBuilder item(ModelItem item) {
        this.item = item;
        return this;
    }

    public ModelBuilder lores(List<String> lores) {
        this.lores = lores;
        return this;
    }

    public ModelBuilder maxFuel(float maxFuel) {
        this.maxFuel = maxFuel;
        return this;
    }

    public ModelBuilder maxSpeed(MaxSpeed maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public ModelBuilder capacity(Capacity capacity) {
        this.capacity = capacity;
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

    public ModelBuilder steering(SteeringLevel steeringLevel) {
        this.steeringLevel = steeringLevel;
        return this;
    }

    public ModelBuilder sound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public ModelBuilder modelPosition(ModelPosition position) {
        this.position = position;
        return this;
    }

    public Model build() {
        if (name == null || maxSpeed == null || item == null ||
                capacity == null || collideBox == null || steeringLevel == null ||
                sound == null || position == null) {
            throw new NullPointerException();
        }
        return new Model(id, item, name, lores, maxFuel, maxSpeed, capacity, steeringLevel, collideBox, height, sound, position);
    }

}
