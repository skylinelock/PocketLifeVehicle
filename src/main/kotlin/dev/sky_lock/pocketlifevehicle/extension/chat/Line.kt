package dev.sky_lock.pocketlifevehicle.extension.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit

class Line {
    private val component = Component.text()

    fun yellow(text: String): Line {
        component.append(Component.text(text, NamedTextColor.YELLOW))
        return this
    }

    fun red(text: String): Line {
        component.append(Component.text(text, NamedTextColor.RED))
        return this
    }

    fun green(text: String): Line {
        component.append(Component.text(text, NamedTextColor.GREEN))
        return this
    }

    fun raw(text: String): Line {
        component.append(Component.text(text))
        return this
    }

    fun colorCoded(text: String): Line {
        component.append(LegacyComponentSerializer.legacyAmpersand().deserialize(text))
        return this
    }

    fun white(text: String): Line {
        component.append(Component.text(text, NamedTextColor.WHITE))
        return this
    }

    fun gold(text: String): Line {
        component.append(Component.text(text, NamedTextColor.GOLD))
        return this
    }

    fun aqua(text: String): Line {
        component.append(Component.text(text, NamedTextColor.AQUA))
        return this
    }

    fun darkAqua(text: String): Line {
        component.append(Component.text(text, NamedTextColor.DARK_AQUA))
        return this
    }

    fun gray(text: String): Line {
        component.append(Component.text(text, NamedTextColor.GRAY))
        return this
    }

    fun grayBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.GRAY, TextDecoration.BOLD)))
        return this
    }

    fun darkGray(text: String): Line {
        component.append(Component.text(text, NamedTextColor.DARK_GRAY))
        return this
    }

    fun darkPurpleBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.DARK_PURPLE, TextDecoration.BOLD)))
        return this
    }

    fun darkGreenBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.DARK_GREEN, TextDecoration.BOLD)))
        return this
    }

    fun goldBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)))
        return this
    }

    fun aquaBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.AQUA, TextDecoration.BOLD)))
        return this
    }

    fun redBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.RED, TextDecoration.BOLD)))
        return this
    }

    fun greenBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.GREEN, TextDecoration.BOLD)))
        return this
    }

    fun yellowBold(text: String): Line {
        component.append(Component.text(text, Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD)))
        return this
    }

    fun broadcast() {
        Bukkit.broadcast(toComponent())
    }

    fun toComponent(): Component {
        return component.build()
    }

}