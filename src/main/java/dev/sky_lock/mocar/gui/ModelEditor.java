package dev.sky_lock.mocar.gui;

import dev.sky_lock.mocar.gui.api.Button;
import dev.sky_lock.mocar.gui.api.GuiType;
import dev.sky_lock.mocar.gui.api.GuiWindow;
import dev.sky_lock.mocar.util.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class ModelEditor extends GuiWindow {

    public ModelEditor(Player player) {
        super("AddModel", player, GuiType.WIDE);
        super.addComponent(new Button(4, new ItemStackBuilder(Material.ENDER_PEARL, 1).name("戻る").build(), (event) -> {
            new CarModelEditor(player).open((Player) event.getWhoClicked());
        }));
        super.addComponent(new Button(20, new ItemStackBuilder(Material.EMERALD_BLOCK, 1).name("ID").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(22, new ItemStackBuilder(Material.NAME_TAG, 1).name("NAME").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(24, new ItemStackBuilder(Material.DIAMOND, 1).name("SPEED").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(29, new ItemStackBuilder(Material.COAL_BLOCK, 1).name("MAXFUEL").build(), (event) -> {
            //TODO:
        }));
        super.addComponent(new Button(31, new ItemStackBuilder(Material.RAILS, 1).name("LORE").build(), (event) -> {
            //TODO;
        }));
        super.addComponent(new Button(33, new ItemStackBuilder(Material.SLIME_BALL, 1).name("LORE").build(), (event) -> {
            //TODO;
        }));
    }

    private void openStringEditor(HumanEntity humanEntity) {
        Player player = (Player) humanEntity;

    }
}
