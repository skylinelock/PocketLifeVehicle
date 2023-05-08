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

    fun withSingleColorCode(text: String): Line {
        val ccIndex = text.lastIndexOf('&')
        if (ccIndex == -1) return raw(text)
        val text = text.substring(ccIndex + 2)
        val colorCode = text.substring(ccIndex, 1)
        val color = parseColor(colorCode) ?: return raw(text)
        component.append(Component.text(text, color))
        return this
    }

    private fun parseColor(text: String): NamedTextColor? {
        var result: ChatColor? = null
        val iterator = text.iterator()
        while (iterator.hasNext()) {
            val char = iterator.nextChar()
            if (char == '&' && iterator.hasNext()) {
                val code = iterator.nextChar()
                result = ChatColor.getByChar(code) ?: result
            }
        }
        if (result == null) return NamedTextColor.WHITE
        return toNamedTextColor(result) ?: NamedTextColor.WHITE
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

    fun darkGray(text: String): Line {
        component.append(Component.text(text, NamedTextColor.DARK_GRAY))
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

    fun toComponent(): Component {
        return component.build()
    }

}