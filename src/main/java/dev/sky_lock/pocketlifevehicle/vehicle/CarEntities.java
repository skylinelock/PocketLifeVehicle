package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.PLVehicle;
import dev.sky_lock.pocketlifevehicle.item.ItemStackBuilder;
import dev.sky_lock.pocketlifevehicle.packet.ActionBar;
import dev.sky_lock.pocketlifevehicle.util.Formats;
import dev.sky_lock.pocketlifevehicle.util.Profiles;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Capacity;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import dev.sky_lock.pocketlifevehicle.vehicle.model.ModelList;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class CarEntities {
    private static final Logger logger = PLVehicle.getInstance().getLogger();
    private static final Map<UUID, Car> entities = new HashMap<>();

    public static boolean spawn(UUID player, Model model, Location location, float fuel) {
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
        Model model = car.getModel();
        ItemStack itemStack = ItemStackBuilder.of(model.getItemStack())
                .tag(PLVehicle.getInstance().createKey("owner"), ItemTagType.STRING, owner.toString())
                .lore("所有者: " + Profiles.getName(owner), "残燃料: " + Formats.truncateToOneDecimalPlace(car.getStatus().getFuel()))
                .itemFlags(ItemFlag.values())
                .build();
        Location location = car.getLocation();
        Item item = location.getWorld().dropItem(car.getLocation(), itemStack);
        item.setMetadata("mocar-fuel", new FixedMetadataValue(PLVehicle.getInstance(), car.getStatus().getFuel()));

        location.getWorld().playSound(location, Sound.BLOCK_IRON_DOOR_OPEN, 1F, 0.2F);
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
            PLVehicle.getInstance().getCarStoreFile().load().forEach(carEntity -> {
                ModelList.of(carEntity.getModelId()).ifPresent(model -> {
                    CarEntities.spawn(carEntity.getOwner(), model, carEntity.getLocation(), carEntity.getFuel());
                });
            });
        } catch (IOException ex) {
            logger.warning("CarEntityの読み込みに失敗しました");
        }
    }

    public static void saveAll() {
        try {
            PLVehicle.getInstance().getCarStoreFile().save(CarEntities.getCarEntities());
            CarEntities.killAll();
        } catch (IOException ex) {
            logger.warning("CarEntityの保存に失敗しました");
        }
    }
}
