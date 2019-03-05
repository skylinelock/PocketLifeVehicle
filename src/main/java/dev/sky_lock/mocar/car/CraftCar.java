package dev.sky_lock.mocar.car;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;

/**
 * @author sky_lock
 */

public class CraftCar extends CraftArmorStand {

    public CraftCar(CraftServer server, EntityArmorStand entity) {
        super(server, entity);
    }
}
