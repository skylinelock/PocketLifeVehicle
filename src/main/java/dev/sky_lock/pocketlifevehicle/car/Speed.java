package dev.sky_lock.pocketlifevehicle.car;

import java.math.BigDecimal;

/**
 * @author sky_lock
 */

class Speed {
    private final static BigDecimal acceleration = new BigDecimal("0.0085");
    private final static BigDecimal frictionalDeceleration = new BigDecimal("0.010");
    private final static BigDecimal magnification = new BigDecimal("0.85");
    private BigDecimal exactSpeed = BigDecimal.ZERO;

    void accelerate() {
        exactSpeed = exactSpeed.add(acceleration);
    }

    void frictionalDecelerate() {
        exactSpeed = exactSpeed.subtract(acceleration);
    }

    void decelerate() {
        exactSpeed = exactSpeed.subtract(acceleration.add(frictionalDeceleration));
    }

    void decrease() {
        exactSpeed = exactSpeed.multiply(magnification);
    }

    boolean isPositive() {
        return exactSpeed.compareTo(BigDecimal.ZERO) > 0;
    }

    boolean isNegative() {
        return exactSpeed.compareTo(BigDecimal.ZERO) < 0;
    }

    float approximate() {
        return exactSpeed.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    boolean isApproximateZero() {
        return approximate() == 0.0F;
    }

    float exact() {
        return exactSpeed.floatValue();
    }

    void zeroize() {
        exactSpeed = BigDecimal.ZERO;
    }

}