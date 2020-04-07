package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author sky_lock
 */

public class BlockChangePacket extends ServerPacket {

    public BlockChangePacket() {
        super(PacketType.Play.Server.BLOCK_CHANGE);
    }

    public void setLocation(Location location) {
        getPacket().getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public void setBlockData(Material type) {
        getPacket().getBlockData().write(0, WrappedBlockData.createData(type));
    }

}
