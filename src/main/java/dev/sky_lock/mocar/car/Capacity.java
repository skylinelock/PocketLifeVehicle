package dev.sky_lock.mocar.car;

import java.util.Arrays;

/**
 * @author sky_lock
 */

public enum Capacity {
    ONE_SEAT(1),
    TWO_SEATS(2),
    FOR_SEATS(4);

    private final int value;

    Capacity(int value) {
        this.value = value;
    }

    static Capacity from(int value) {
        return Arrays.stream(values()).filter(capacity -> capacity.value() == value).findFirst().orElse(null);
    }

    public int value() {
        return value;
    }
}
