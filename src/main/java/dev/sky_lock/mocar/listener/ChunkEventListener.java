package dev.sky_lock.mocar.listener;

import dev.sky_lock.mocar.car.Car;
import dev.sky_lock.mocar.car.CarArmorStand;
import dev.sky_lock.mocar.car.CarEntities;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class ChunkEventListener implements Listener {
    private final List<Car> cars = new ArrayList<>();

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Arrays.stream(event.getChunk().getEntities())
                .filter(entity -> entity instanceof CarArmorStand.CraftCar)
                .forEach(entity -> {
                    Car car = CarEntities.getCar(((CarArmorStand) ((CarArmorStand.CraftCar) entity).getHandle()));
                    car.kill();
                    cars.add(car);
                });
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        cars.stream().filter(car -> isSameChunk(car.getLocation().getChunk(), event.getChunk()))
                .collect(Collectors.toList()).removeIf(CarEntities::spawn);
    }

    private boolean isSameChunk(Chunk chunk1, Chunk chunk2) {
        return chunk1.getX() == chunk2.getX() && chunk1.getZ() == chunk2.getZ();
    }

}
