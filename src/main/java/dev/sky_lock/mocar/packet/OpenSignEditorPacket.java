package dev.sky_lock.mocar.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.BlockPosition;

/**
 * @author sky_lock
 */

public class OpenSignEditorPacket extends ServerPacket {

    public OpenSignEditorPacket() {
        super(PacketType.Play.Server.OPEN_SIGN_EDITOR);
    }

    public void setBlockPosition(BlockPosition position) {
        getPacket().getBlockPositionModifier().write(0, position);
    }

}
