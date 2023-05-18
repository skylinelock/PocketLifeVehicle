package dev.sky_lock.pocketlifevehicle.task

import dev.sky_lock.pocketlifevehicle.text.Line
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player

/**
 * @author sky_lock
 */
open class AbstractWarning {
    var count = 0

    fun send(player: Player, subTitle: Line) {
        val title = Title.title(WARNING.toComponent(), subTitle.toComponent())
        player.showTitle(title)
    }

    fun stop(player: Player) {
        player.resetTitle()
    }

    companion object {
        private val WARNING = Line().red("⚠⚠WARNING⚠⚠")
    }
}