package dev.sky_lock.mocar.car;

import dev.sky_lock.mocar.packet.ActionBar;
import dev.sky_lock.mocar.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

/**
 * @author sky_lock
 */

class MeterPanel {
    private final CarStatus status;
    private final CarModel model;
    private final Engine engine;

    MeterPanel(CarStatus status, CarModel model, Engine engine) {
        this.status = status;
        this.model = model;
        this.engine = engine;
    }

    void display(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.RED).append(ChatColor.BOLD).append("E ").append(ChatColor.GREEN);

        float fuelRate = status.getFuel() / model.getMaxFuel();
        int filled = Math.round(70 * fuelRate);

        IntStream.range(0, filled).forEach(count -> builder.append("ǀ"));
        builder.append(ChatColor.RED);
        IntStream.range(0, 70 - filled).forEach(count -> builder.append("ǀ"));

        builder.append(" ").append(ChatColor.GREEN).append(ChatColor.BOLD).append(" F").append("   ").append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD);
        if (status.getSpeed().isApproximateZero()) {
            builder.append("N");
        } else {
            if (status.getSpeed().isPositive()) {
                builder.append("D");
            } else if (status.getSpeed().isNegative()) {
                builder.append("R");
            }
        }
        builder.append("   ");
        builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD);
        String blockPerSecond = StringUtil.formatDecimal(Math.abs(engine.speedPerSecond()));
        builder.append(blockPerSecond).append(ChatColor.GRAY).append(ChatColor.BOLD).append(" blocks/s");

        ActionBar.sendPacket(player, builder.toString());
    }
}
