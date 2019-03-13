package dev.sky_lock.mocar.gui.api;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author sky_lock
 */

public class PagingGui {
    private final List<GuiWindow> windows;

    public PagingGui(GuiWindow... window) {
        this.windows = Arrays.asList(window);
    }

    public void open(Player player) {
        windows.get(0).open(player);
    }

}
