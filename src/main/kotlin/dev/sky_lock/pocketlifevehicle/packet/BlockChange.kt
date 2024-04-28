package dev.sky_lock.pocketlifevehicle.packet

import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.Material

/**
 * @author sky_lock
 */

class BlockChange(private val packet: WrapperPlayServerBlockChange) :
    ServerPacket<WrapperPlayServerBlockChange>(packet) {

    constructor(x: Int, y: Int, z: Int, material: Material) : this(WrapperPlayServerBlockChange(Vector3i(x, y, z), SpigotConversionUtil.fromBukkitBlockData(material.createBlockData()).globalId))

}