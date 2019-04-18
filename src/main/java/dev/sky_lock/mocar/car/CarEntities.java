package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.MoCar;
import dev.sky_lock.packetcontrol.ActionBar;
import dev.sky_lock.mocar.util.Profiles;
import dev.sky_lock.mocar.util.StringUtil;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class CarEntities {
    private static final Logger logger = MoCar.getInstance().getLogger();
    private static final Map<UUID, Car> entities = new HashMap<>();

    public static boolean spawn(UUID player, CarModel model, Location location, float fuel) {
        if (player == null || model == null || location == null) {
            return false;
        }
        if (location.getBlock().getType() != Material.AIR) {
            ActionBar.sendPacket(Bukkit.getPlayer(player), ChatColor.RED + "ブロックがあるので車を設置できません");
            return false;
        }

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

    public static boolean spawn(Car car) {
        return getOwner(car).map(owner -> spawn(owner, car.getModel(), car.getLocation(), car.getStatus().getFuel())).orElse(false);
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

    private static void killAll() {
        entities.values().forEach(Car::kill);
    }

    public static void tow(Car car) {
        getOwner(car).ifPresent(owner -> tow(owner, car));
    }

    public static void tow(UUID owner) {
        Optional.ofNullable(entities.get(owner)).ifPresent(car -> tow(owner, car));
    }

    private static void tow(UUID owner, Car car) {
        CarModel model = car.getModel();
        ItemStack itemStack = model.getItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList("Owner : " + Profiles.getName(owner), "Fuel  : " + StringUtil.formatDecimal(car.getStatus().getFuel())));
        itemStack.setItemMeta(meta);
        Location location = car.getLocation();
        Item item = location.getWorld().dropItem(car.getLocation(), itemStack);
        location.getWorld().playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1F, 0.2F);
        item.setMetadata("mocar-fuel", new FixedMetadataValue(MoCar.getInstance(), car.getStatus().getFuel()));
        kill(owner);
    }

    public static Car getCar(SeatArmorStand seat) {
        return entities.values().stream().filter(car -> car.contains(seat)).findFirst().orElse(null);
    }

    public static Car getCar(CarArmorStand basis) {
        return entities.values().stream().filter(car -> car.contains(basis)).findFirst().orElse(null);
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
                ModelList.of(carEntity.getModelID()).ifPresent(model -> {
                    CarEntities.spawn(carEntity.getOwner(), model, carEntity.getLocation(), carEntity.getFuel());
                });
            });
        } catch (IOException ex) {
            logger.warning("CarEntityの読み込みに失敗しました");
        }
    }

    public static void saveAll() {
        try {
            MoCar.getInstance().getCarStoreFile().save(CarEntities.getCarEntities());
            CarEntities.killAll();
        } catch (IOException ex) {
            logger.warning("CarEntityの保存に失敗しました");
        }
    }
}
