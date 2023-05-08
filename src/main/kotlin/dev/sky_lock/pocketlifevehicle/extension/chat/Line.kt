package dev.sky_lock.pocketlifevehicle.extension.chat

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor

class Line {
    private val component = Component.text()

    private val namedTextColors = mapOf(
            ChatColor.BLACK to NamedTextColor.BLACK,
            ChatColor.DARK_BLUE to NamedTextColor.DARK_BLUE,
            ChatColor.DARK_GREEN to NamedTextColor.DARK_GREEN,
            ChatColor.DARK_AQUA to NamedTextColor.DARK_AQUA,
            ChatColor.DARK_RED to NamedTextColor.DARK_RED,
            ChatColor.DARK_PURPLE to NamedTextColor.DARK_PURPLE,
            ChatColor.GOLD to NamedTextColor.GOLD,
            ChatColor.GRAY to NamedTextColor.GRAY,
            ChatColor.DARK_GRAY to NamedTextColor.DARK_GRAY,
            ChatColor.BLUE to NamedTextColor.BLUE,
            ChatColor.GREEN to NamedTextColor.GREEN,
            ChatColor.AQUA to NamedTextColor.AQUA,
            ChatColor.RED to NamedTextColor.RED,
            ChatColor.LIGHT_PURPLE to NamedTextColor.LIGHT_PURPLE,
            ChatColor.YELLOW to NamedTextColor.YELLOW,
            ChatColor.WHITE to NamedTextColor.WHITE
    )

    private fun toNamedTextColor(color: ChatColor): NamedTextColor? {
        return namedTextColors[color]
    }

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
        val bars = text.split("&")
        var firstChecked = false
        for (bar in bars) {
            if (bar.length < 2) {
                raw(bar)
                continue
            }
            if (firstChecked) {
                component.append(Component.text(bar.substring(1), parseColor(bar[0])))
                continue
            }
            if (text.startsWith("&")) {
                component.append(Component.text(bar.substring(1), parseColor(bar[0])))
            } else {
                raw(bar)
            }
            firstChecked = true
        }
        return this
    }

    private fun parseColor(char: Char): NamedTextColor {
        val result = ChatColor.getByChar(char) ?: return NamedTextColor.WHITE
        return toNamedTextColor(result) ?: NamedTextColor.WHITE
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

    fun toComponent(): Component {
        return component.build()
    }

}