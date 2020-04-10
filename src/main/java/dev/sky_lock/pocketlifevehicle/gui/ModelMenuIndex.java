package dev.sky_lock.pocketlifevehicle.gui;

/**
 * @author sky_lock
 */

public enum ModelMenuIndex {

    MAIN_MENU(1),
    SETTING(2),
    SPEED(3),
    CAR_ITEM(4),
    FUEL(5),
    CONFIRM(6),
    CAPACITY(7),
    COLLIDE_BOX(8),
    COLLIDE_BASESIDE(9),
    COLLIDE_HEIGHT(10),
    HEIGHT(11),
    STEERING(12),
    SOUND(13);


    private final int value;

    ModelMenuIndex(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
