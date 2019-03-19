package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.item.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class CarItemSelector extends GuiWindow {


    public CarItemSelector(Player holder) {
        super("CarItemSelector", holder, GuiType.SMALL);
        super.addComponent(new Button(9, new ItemStackBuilder(Material.WOOD_HOE, 1).build(), (event) -> {
            new DurabilitySelector(holder, Material.WOOD_HOE).open(holder);
        }));
        super.addComponent(new Button(11, new ItemStackBuilder(Material.STONE_HOE, 1).build(), (event) -> {
            new DurabilitySelector(holder, Material.STONE_HOE).open(holder);
        }));
        super.addComponent(new Button(13, new ItemStackBuilder(Material.IRON_HOE, 1).build(), (event) -> {
            new DurabilitySelector(holder, Material.IRON_HOE).open(holder);
        }));
        super.addComponent(new Button(15, new ItemStackBuilder(Material.GOLD_HOE, 1).build(), (event) -> {
            new DurabilitySelector(holder, Material.GOLD_HOE).open(holder);
        }));
        super.addComponent(new Button(17, new ItemStackBuilder(Material.DIAMOND_HOE, 1).build(), (event) -> {
            new DurabilitySelector(holder, Material.DIAMOND_HOE).open(holder);
        }));
    }
}
