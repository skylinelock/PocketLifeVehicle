package dev.sky_lock.mocar.gui;

/**
 * @author sky_lock
 */

public enum ModelMenuIndex {

    MAIN_MENU(1),
    SETTING(2),
    SPEED(3),
    CAR_ITEM(4),
    FUEL(5),
    WOODEN_HOE(6),
    STONE_HOE(7),
    IRON_HOE(8),
    GOLDEN_HOE(9),
    DIAMOND_HOE(10),
    CONFIRM(11),
    CAPACITY(12),
    COLLIDE_BOX(13),
    COLLIDE_BASESIDE(14),
    COLLIDE_HEIGHT(15),
    HEIGHT(16),
    STEERING(17),
    SOUND(18);


    private final int value;

    ModelMenuIndex(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
