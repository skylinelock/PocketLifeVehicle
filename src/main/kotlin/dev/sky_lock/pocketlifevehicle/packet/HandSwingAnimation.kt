package dev.sky_lock.pocketlifevehicle.packet

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType

/**
 * @author sky_lock
 */

class HandSwingAnimation(private val packet: WrapperPlayServerEntityAnimation): ServerPacket<WrapperPlayServerEntityAnimation>(packet) {
    constructor(entityId: Int) : this(WrapperPlayServerEntityAnimation(entityId, WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM))

    fun setHand(type: HandType) {
        packet.type = type.type
    }

    enum class HandType(val type: EntityAnimationType) {
        MAIN_HAND(EntityAnimationType.SWING_MAIN_ARM),
        OFF_HAND(EntityAnimationType.SWING_OFF_HAND)
    }
}