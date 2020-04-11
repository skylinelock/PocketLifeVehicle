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
    CAPACITY(6),
    COLLIDE_BOX(7),
    COLLIDE_BASESIDE(8),
    COLLIDE_HEIGHT(9),
    HEIGHT(10),
    ITEM_POSITION(11),
    SOUND(12),
    STEERING(13);

    private final int value;

    ModelMenuIndex(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
