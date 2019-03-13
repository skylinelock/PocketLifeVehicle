package dev.sky_lock.mocar.gui.api;

import org.bukkit.entity.Player;

/**
 * @author sky_lock
 */

public class PagingWindow extends GuiWindow {
    private final GuiWindow previous;
    private final GuiWindow next;

    public PagingWindow(String title, Player holder, GuiType type, GuiWindow previous, GuiWindow next) {
        super(title, holder, type);
        this.previous = previous;
        this.next = next;
    }


}
