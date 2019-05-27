package dev.sky_lock.packet;

import com.comphenix.protocol.PacketType;

/**
 * @author sky_lock
 */

public class AnimationPacket extends ServerPacket {

    public AnimationPacket() {
        super(PacketType.Play.Server.ANIMATION);
    }

    public void setEntityID(int value) {
        getPacket().getIntegers().write(0, value);
    }

    public void setAnimation(AnimationType type) {
        getPacket().getIntegers().write(1, type.value());
    }

    public enum AnimationType {
        SWING_MAIN_ARM(0),
        TAKE_DAMAGE(1),
        LEAVE_BED(2),
        SWING_OFFHAND(3),
        CRITICAL_EFFECT(4),
        MAGIC_CRITICAL_EFFECT(5);

        private final int id;

        AnimationType(int id) {
            this.id = id;
        }

        int value() {
            return id;
        }
    }
}
