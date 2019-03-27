package dev.sky_lock.mocar.car;


import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;

/**
 * @author sky_lock
 */

public class CraftCar extends CraftArmorStand {

    CraftCar(CraftServer server, CarArmorStand entity) {
        super(server, entity);
    }
}
