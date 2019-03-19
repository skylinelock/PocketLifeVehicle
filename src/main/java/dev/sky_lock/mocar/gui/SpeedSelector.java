package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.car.Speed;
import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class SpeedSelector extends GuiWindow {

    public SpeedSelector(Player holder) {
        super("SpeedSelector", holder, GuiType.BIG);
        super.addComponent(new Button(11, new ItemStackBuilder(Material.SEA_LANTERN, 1).name("SLOWEST").build(), (event) -> {
            EditSessions.get(holder.getUniqueId()).setSpeed(Speed.SLOWEST);
            new ModelSetting(holder).open(holder);
        }));
        super.addComponent(new Button(13, new ItemStackBuilder(Material.SEA_LANTERN, 1).name("SLOW").build(), (event) -> {
            EditSessions.get(holder.getUniqueId()).setSpeed(Speed.SLOW);
            new ModelSetting(holder).open(holder);
        }));
        super.addComponent(new Button(15, new ItemStackBuilder(Material.SEA_LANTERN, 1).name("NORMAL").build(), (event) -> {
            EditSessions.get(holder.getUniqueId()).setSpeed(Speed.NORMAL);
            new ModelSetting(holder).open(holder);
        }));
        super.addComponent(new Button(29, new ItemStackBuilder(Material.SEA_LANTERN, 1).name("FAST").build(), (event) -> {
            EditSessions.get(holder.getUniqueId()).setSpeed(Speed.FAST);
            new ModelSetting(holder).open(holder);
        }));
        super.addComponent(new Button(31, new ItemStackBuilder(Material.SEA_LANTERN, 1).name("FASTEST").build(), (event) -> {
            EditSessions.get(holder.getUniqueId()).setSpeed(Speed.FASTEST);
            new ModelSetting(holder).open(holder);
        }));
    }
}
