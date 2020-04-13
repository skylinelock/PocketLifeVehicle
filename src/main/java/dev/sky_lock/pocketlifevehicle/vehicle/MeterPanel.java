package dev.sky_lock.pocketlifevehicle.vehicle;

import dev.sky_lock.pocketlifevehicle.util.Formats;
import dev.sky_lock.pocketlifevehicle.vehicle.model.Model;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

/**
 * @author sky_lock
 */

class MeterPanel {
    private final CarStatus status;
    private final Model model;
    private final Engine engine;

    MeterPanel(CarStatus status, Model model, Engine engine) {
        this.status = status;
        this.model = model;
        this.engine = engine;
    }

    void display(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.RED).append(ChatColor.BOLD).append("E ").append(ChatColor.GREEN);

        float fuelRate = status.getFuel() / model.getSpec().getMaxFuel();
        int filled = Math.round(70 * fuelRate);

        IntStream.range(0, filled).forEach(count -> builder.append("ǀ"));
        builder.append(ChatColor.RED);
        IntStream.range(0, 70 - filled).forEach(count -> builder.append("ǀ"));

        builder.append(" ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(" F").append("   ").append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD);
        if (status.getSpeed().isApproximateZero()) {
            builder.append("P");
        } else {
            if (status.getSpeed().isPositive()) {
                builder.append("D");
            } else if (status.getSpeed().isNegative()) {
                builder.append("R");
            }
        }
        builder.append("   ");
        builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD);
        String blockPerSecond = Formats.truncateToOneDecimalPlace(Math.abs(engine.speedPerSecond()));
        builder.append(blockPerSecond).append(ChatColor.GRAY).append(ChatColor.BOLD).append(" blocks/s");

        player.sendActionBar(builder.toString());
    }
}
