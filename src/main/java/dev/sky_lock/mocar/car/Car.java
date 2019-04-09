package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.gui.CarUtilMenu;
import dev.sky_lock.mocar.packet.FakeExplosionPacket;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sky_lock
 */

public class Car {
    final List<SeatArmorStand> seats = new ArrayList<>();
    private final CarModel model;
    private CarArmorStand center;
    private CarUtilMenu menu;
    private CarStatus status;
    private CarSoundTask sound;

    private Engine engine;
    private Steering steering;
    private MeterPanel meterPanel;
    private boolean beginExplode = false;

    Car(CarModel model) {
        this.model = model;
        this.status = new CarStatus();
        this.engine = new Engine(status, model);
        this.steering = new Steering(status);
        this.meterPanel = new MeterPanel(status, model, engine);
        this.sound = new CarSoundTask(model, status);
    }

    public CarSoundTask getSound() {
        return sound;
    }

    public CarStatus getStatus() {
        return status;
    }

    Engine getEngine() {
        return engine;
    }

    Steering getSteering() {
        return steering;
    }

    MeterPanel getMeterPanel() {
        return meterPanel;
    }

    CarArmorStand getCenter() {
        return center;
    }

    public CarModel getModel() {
        return model;
    }

    void addSeat(SeatArmorStand seat) {
        seats.add(seat);
    }

    public Location getLocation() {
        return center.getLocation();
    }

    boolean isBeginExplode() {
        return beginExplode;
    }

    void setBeginExplode(boolean beginExplode) {
        this.beginExplode = beginExplode;
    }

    public boolean refuel(float fuel) {
        return engine.refuel(fuel);
    }

    public void openMenu(Player player) {
        if (menu == null) {
            menu = new CarUtilMenu(player, this);
        }
        menu.open(player, 1);
    }

    public void closeMenu(Player player) {
        if (menu == null) {
            return;
        }
        menu.close(player);
    }

    void spawn(Location location) {
        center = new CarArmorStand(((CraftWorld) location.getWorld()).getHandle());
        getStatus().setLocation(location);
        center.assemble(this);
        center.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0F);
        status.setYaw(location.getYaw());
        World world = center.world;
        world.addEntity(center);
    }

    public boolean isInWater() {
        return center.isInWater();
    }

    public boolean contains(SeatArmorStand seat) {
        return this.seats.contains(seat);
    }

    public List<Player> getPassengers() {
        return seats.stream().filter(seat -> !seat.passengers.isEmpty()).map(seat -> (CraftPlayer) seat.passengers.get(0).getBukkitEntity()).collect(Collectors.toList());
    }

    public Optional<Player> getDriver() {
        return seats.stream().filter(seat -> seat.isDriverSheet() && !seat.passengers.isEmpty()).findFirst().map(seat -> seat.getPassenger().orElse(null));
    }

    void kill() {
        center.killEntity();
        seats.forEach(SeatArmorStand::killEntity);
    }

    public void explode() {
        FakeExplosionPacket explosion = new FakeExplosionPacket();
        explosion.setX(getLocation().getX());
        explosion.setY(getLocation().getY());
        explosion.setZ(getLocation().getZ());
        explosion.setRadius(5);
        explosion.broadCast();
        getLocation().getWorld().playSound(getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
    }


}