package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.util.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
        if (location.getBlock() == null || location.getBlock().getType() != Material.AIR) {
            ActionBar.sendPacket(Bukkit.getPlayer(player), ChatColor.RED + "ブロックがあるので車を設置できません");
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

    public static void kill(CarArmorStand armorStand) {
        if (entities.containsValue(armorStand)) {
            entities.values().remove(armorStand);
            armorStand.killEntity();
        }
    }

    public static void tow(UUID owner) {
        Optional.ofNullable(entities.get(owner)).ifPresent(car -> {
            tow(owner, car);
        });
    }

    public static void tow(UUID owner, CarArmorStand car) {
        CarModel model = car.getModel();
        CarItem carItem = model.getItem();
        ItemStack itemStack = carItem.getStack(model.getName());
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList("Owner : " + PlayerInfo.getName(owner), "Fuel : " + car.getStatus().getFuel()));
        itemStack.setItemMeta(meta);
        Item item = car.getBukkitEntity().getWorld().dropItem(car.getLocation(), itemStack);
        item.setMetadata("mocar-fuel", new FixedMetadataValue(MoCar.getInstance(), car.getStatus().getFuel()));
        kill(owner);
    }

    public static Set<CarEntity> getCarEntities() {
        Set<CarEntity> carEntities = new HashSet<>();
        entities.forEach((key, value) -> carEntities.add(new CarEntity(key.toString(), value.getModel(), value.getLocation(), value.getStatus().getFuel())));
        return carEntities;
    }

    public static CarArmorStand get(UUID player) {
        return entities.get(player);
    }

    public static Optional<UUID> getOwner(CarArmorStand car) {
        return entities.entrySet().stream().filter(entry -> entry.getValue().equals(car)).findFirst().map(Map.Entry::getKey);
    }
}
