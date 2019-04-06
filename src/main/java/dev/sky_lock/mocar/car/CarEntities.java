package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.util.PlayerInfo;
import dev.sky_lock.mocar.util.StringUtil;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class CarEntities {
    private static final Map<UUID, Car> entities = new HashMap<>();

    public static boolean spawn(UUID player, CarModel model, Location location, float fuel) {
        if (player == null || model == null || location == null) {
            return false;
        }
        if (location.getBlock().getType() != Material.AIR) {
            ActionBar.sendPacket(Bukkit.getPlayer(player), ChatColor.RED + "ブロックがあるので車を設置できません");
            return false;
        }
        World worldHandle = ((CraftWorld) location.getWorld()).getHandle();

        Car car = null;
        if (model.getCapacity() == Capacity.ONE_SEAT) {
            car = new OneSeatCar(model);
        } else if (model.getCapacity() == Capacity.TWO_SEATS) {
            car = new TwoSeatsCar(model);
        } else if (model.getCapacity() == Capacity.FOR_SEATS) {
            car = new FourSeatsCar(model);
        }
        if (car == null) {
            return false;
        }
        car.getStatus().setFuel(fuel);
        car.spawn(location);
        kill(player);

        entities.put(player, car);
        return true;
    }

    public static void kill(UUID owner) {
        if (entities.containsKey(owner)) {
            Car car = entities.remove(owner);
            car.kill();
        }
    }

    public static void kill(Car car) {
        if (entities.containsValue(car)) {
            entities.values().remove(car);
            car.kill();
        }
    }

    static void killAll() {
        entities.values().forEach(Car::kill);
    }

    public static void tow(Car car) {
        getOwner(car).ifPresent(owner -> {
            tow(owner, car);
        });
    }

    public static void tow(UUID owner) {
        Optional.ofNullable(entities.get(owner)).ifPresent(car -> tow(owner, car));
    }

    private static void tow(UUID owner, Car car) {
        CarModel model = car.getModel();
        CarItem carItem = model.getItem();
        ItemStack itemStack = carItem.getStack(model.getName());
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList("Owner : " + PlayerInfo.getName(owner), "Fuel  : " + StringUtil.formatDecimal(car.getStatus().getFuel())));
        itemStack.setItemMeta(meta);
        Item item = car.getLocation().getWorld().dropItem(car.getLocation(), itemStack);
        item.setMetadata("mocar-fuel", new FixedMetadataValue(MoCar.getInstance(), car.getStatus().getFuel()));
        kill(owner);
    }

    public static Car getCar(SeatArmorStand seat) {
        return entities.values().stream().filter(abstractCar -> abstractCar.contains(seat)).findFirst().orElse(null);
    }

    static Set<CarEntity> getCarEntities() {
        return entities.entrySet().stream().map(entry -> new CarEntity(entry.getKey().toString(), entry.getValue().getModel().getId(), entry.getValue().getLocation(), entry.getValue().getStatus().getFuel())).collect(Collectors.toSet());
    }

    public static Car of(UUID player) {
        return entities.get(player);
    }

    public static Optional<UUID> getOwner(Car car) {
        return entities.entrySet().stream().filter(entry -> entry.getValue().equals(car)).findFirst().map(Map.Entry::getKey);
    }

    public static void spawnAll() {
        try {
            MoCar.getInstance().getCarStoreFile().load().forEach(carEntity -> {
                ModelList.get(carEntity.getModelID()).ifPresent(model -> {
                    CarEntities.spawn(carEntity.getOwner(), model, carEntity.getLocation(), carEntity.getFuel());
                });
            });
        } catch (IOException ex) {
            Bukkit.getLogger().warning("CarEntityの読み込みに失敗しました");
        }
    }

    public static void saveAll() {
        try {
            MoCar.getInstance().getCarStoreFile().save(CarEntities.getCarEntities());
            CarEntities.killAll();
        } catch (IOException ex) {
            Bukkit.getLogger().warning("CarEntityの保存に失敗しました");
        }
    }
}
