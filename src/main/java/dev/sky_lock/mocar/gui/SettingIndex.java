package dev.sky_lock.mocar.gui;

/**
 * @author sky_lock
 */

public enum SettingIndex {

    MAIN_MENU(1),
    SPEED(2),
    CAR_ITEM(3),
    FUEL(4),
    WOODEN_HOE(5),
    STONE_HOE(6),
    IRON_HOE(7),
    GOLDEN_HOE(8),
    DIAMOND_HOE(9),
    CONFIRM(10);


    private final int value;

    SettingIndex(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
