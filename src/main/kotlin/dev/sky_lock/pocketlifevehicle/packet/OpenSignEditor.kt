package dev.sky_lock.pocketlifevehicle.packet

import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor

/**
 * @author sky_lock
 */

class OpenSignEditor(private val packet: WrapperPlayServerOpenSignEditor) :
    ServerPacket<WrapperPlayServerOpenSignEditor>(packet) {

    constructor(x: Int, y: Int, z: Int) : this(WrapperPlayServerOpenSignEditor(Vector3i(x, y, z), true))

}