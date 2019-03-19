package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * @author sky_lock
 */

public class CarEntities {
    private static final Map<UUID, CarArmorStand> entities = new HashMap<>();

    public static boolean spawn(UUID player, CarModel model, Location location, float fuel) {
        if (player == null || model == null || location == null) {
            return false;
        }
        CarArmorStand armorStand = new CarArmorStand(((CraftWorld) location.getWorld()).getHandle(), model, new CarStatus());
        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        ((CraftWorld) location.getWorld()).getHandle().addEntity(armorStand);
        armorStand.getStatus().setFuel(fuel);

        kill(player);

        entities.put(player, armorStand);
        return true;
    }

    public static void kill(UUID owner) {
        if (entities.containsKey(owner)) {
            CarArmorStand armorStand = entities.remove(owner);
            armorStand.killEntity();
        }
    }

    public static void tow(UUID uuid) {
        Optional.ofNullable(entities.get(uuid)).ifPresent(car -> {
            CarModel model = car.getModel();
            CarItem carItem = model.getItem();
            ItemStack itemStack = carItem.getStack(model.getName());
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(Collections.singletonList("Fuel : " + car.getStatus().getFuel()));
            itemStack.setItemMeta(meta);
            Item item = car.getBukkitEntity().getWorld().dropItem(car.getLocation(), itemStack);
            item.setMetadata("mocar-fuel", new FixedMetadataValue(MoCar.getInstance(), car.getStatus().getFuel()));
        });
        kill(uuid);
    }

    public static CarArmorStand get(UUID player) {
        return entities.get(player);
    }

    public static UUID getOwner(CarArmorStand car) {
        for (Map.Entry<UUID, CarArmorStand> entry : entities.entrySet()) {
            if (entry.getValue().equals(car)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
