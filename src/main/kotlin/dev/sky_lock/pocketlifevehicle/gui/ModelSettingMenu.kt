package dev.sky_lock.pocketlifevehicle.gui

import dev.sky_lock.menu.InventoryMenu
import dev.sky_lock.pocketlifevehicle.gui.contents.*
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
class ModelSettingMenu(player: Player) : InventoryMenu("モデル設定", Type.BIG, player) {
    init {
        addContents(EditCarModelContents(player))
        addContents(ModelSettingContents(player))
        addContents(FuelContents(player))
        addContents(SpeedContents(player))
        addContents(CapacityContents(player))
        addContents(SteeringLevelContents(player))
        addContents(ItemOptionContents(player))
        addContents(ItemSelectContents(player))
        addContents(ItemPositionContents(player))
        addContents(CollideBoxContents(player))
        addContents(BaseSideContents(player))
        addContents(CollideHeightContents(player))
        addContents(SoundContents(player))
    }
}