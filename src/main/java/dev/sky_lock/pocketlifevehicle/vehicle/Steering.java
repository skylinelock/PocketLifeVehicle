package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.packet.AnimationPacket;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MainHand;

/**
 * @author sky_lock
 */

class Steering {
    private final CarStatus status;

    Steering(CarStatus status) {
        this.status = status;
    }

    void right(Player driver) {
        if (Math.round(status.getFuel()) == 0 || status.getSpeed().isApproximateZero()) {
            return;
        }
        if (status.getSpeed().isPositive()) {
            status.setYaw(status.getYaw() + 4.0F);
        } else {
            status.setYaw(status.getYaw() - 4.0F);
        }
        if (status.isWieldHand()) {
            raiseLeftArm(driver);
        }
    }

    void left(Player driver) {
        if (Math.round(status.getFuel()) == 0 ||status.getSpeed().isApproximateZero()) {
            return;
        }
        if (status.getSpeed().isPositive()) {
            status.setYaw(status.getYaw() - 4.0F);
        } else {
            status.setYaw(status.getYaw() + 4.0F);
        }
        if (status.isWieldHand()) {
            raiseRightArm(driver);
        }
    }

    private void raiseLeftArm(Player player) {
        if (player.getMainHand() == MainHand.RIGHT) {
            raiseOffhand(player.getEntityId());
        } else {
            raiseMainHand(player.getEntityId());
        }
    }

    private void raiseRightArm(Player player) {
        if (player.getMainHand() == MainHand.RIGHT) {
            raiseMainHand(player.getEntityId());
        } else {
            raiseOffhand(player.getEntityId());
        }
    }

    private void raiseMainHand(int entityID) {
        AnimationPacket packet = new AnimationPacket();
        packet.setEntityID(entityID);
        packet.setAnimationType(AnimationPacket.AnimationType.SWING_MAIN_ARM);
        packet.broadCast();
    }

    private void raiseOffhand(int entityID) {
        AnimationPacket packet = new AnimationPacket();
        packet.setEntityID(entityID);
        packet.setAnimationType(AnimationPacket.AnimationType.SWING_OFFHAND);
        packet.broadCast();
    }
}
